using System;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Threading;
using Tmon.Simplex.Channels;

namespace Tmon.Simplex.Extensions
{
    /// <summary>
    /// https://github.com/dotnet/reactive/issues/395
    /// 의 소스를 그대로 가져다 사용한 것입니다.
    /// </summary>
    public static class ObservablesExtension
    {
        public static IObservable<T> ThrottleFirst<T>(this IObservable<T> source,
                TimeSpan timespan, IScheduler timeSource)
        {
            return new ThrottleFirstObservable<T>(source, timeSource, timespan);
        }

        internal static IObservable<(Channel, T)> Wrap<T>(this IObservable<T> source, Channel channel)
        {
            return source.SelectMany(x => Observable.Return((channel, x)));
        }

        internal static IObservable<T> Unwrap<T>(this IObservable<(Channel channel, T result)> source)
        {
            return source.SelectMany(x => Observable.Return(x.result));
        }
    }

    sealed class ThrottleFirstObservable<T> : IObservable<T>
    {
        readonly IObservable<T> source;

        readonly IScheduler timeSource;

        readonly TimeSpan timespan;

        internal ThrottleFirstObservable(IObservable<T> source,
                  IScheduler timeSource, TimeSpan timespan)
        {
            this.source = source;
            this.timeSource = timeSource;
            this.timespan = timespan;
        }

        public IDisposable Subscribe(IObserver<T> observer)
        {
            var parent = new ThrottleFirstObserver(observer, timeSource, timespan);
            var d = source.Subscribe(parent);
            parent.OnSubscribe(d);
            return d;
        }

        sealed class ThrottleFirstObserver : IDisposable, IObserver<T>
        {
            readonly IObserver<T> downstream;

            readonly IScheduler timeSource;

            readonly TimeSpan timespan;

            IDisposable upstream;

            T queued;

            bool once;

            double due;

            internal ThrottleFirstObserver(IObserver<T> downstream,
                    IScheduler timeSource, TimeSpan timespan)
            {
                this.downstream = downstream;
                this.timeSource = timeSource;
                this.timespan = timespan;
            }

            public void OnSubscribe(IDisposable d)
            {
                if (Interlocked.CompareExchange(ref upstream, d, null) != null)
                {
                    d.Dispose();
                }
            }

            public void Dispose()
            {
                var d = Interlocked.Exchange(ref upstream, this);
                if (d != null && d != this)
                {
                    d.Dispose();
                }
            }

            public void OnCompleted()
            {
                downstream.OnCompleted();
            }

            public void OnError(Exception error)
            {
                downstream.OnError(error);
            }

            //public void OnNext(T value)
            //{
            //    var now = timeSource.Now.ToUnixTimeMilliseconds();
            //    if (!once)
            //    {
            //        once = true;
            //        due = now + timespan.TotalMilliseconds;
            //        downstream.OnNext(value);
            //    }
            //    else if (now >= due)
            //    {
            //        due = now + timespan.TotalMilliseconds;
            //        downstream.OnNext(value);
            //    }

            //}
            public void OnNext(T value)
            {
                var now = timeSource.Now.ToUnixTimeMilliseconds();
                if (!once)
                {
                    queued = default(T);
                    once = true;
                    due = now + timespan.TotalMilliseconds;
                    downstream.OnNext(value);
                }
                else if (now >= due)
                {
                    queued = default(T);
                    due = now + timespan.TotalMilliseconds;
                    downstream.OnNext(value);
                }
                else
                {
                    bool firstQueue = queued == null;
                    queued = value;
                    if (firstQueue)
                    {
                        timeSource.Schedule(due - now, (IScheduler s, double d) =>
                        {
                            OnNext(queued);
                            return null;
                        });
                    }
                }
            }
        }
    }

}
