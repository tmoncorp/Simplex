using System;
using System.Collections;
using System.Collections.Concurrent;
using System.Diagnostics;
using System.Linq;
using System.Reactive;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using System.Reflection;
using System.Threading;
using Tmon.Simplex.Actions;
using Tmon.Simplex.Attributes;
using Tmon.Simplex.Channels;
using Tmon.Simplex.Extensions;

namespace Tmon.Simplex.Store
{
    public class Store<TActionBinderSet> : IActionStore<TActionBinderSet>
        where TActionBinderSet : class, IActionBinderSet, new()
    {
        protected TActionBinderSet Actions { get; } = new TActionBinderSet();

        private readonly ConcurrentDictionary<string, object> observableSources;

        internal Store()
        {
            observableSources = new ConcurrentDictionary<string, object>();
        }
        
        public void Dispatch<TAction, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            Action begin = null,
            Action end = null,
            Func<TAction, Channel> channel = null,
            CancellationToken? cancellationToken = null)
            where TAction : IAction<TResult>
            => DispatchInner(action, parameter: default(object), channel: channel, begin: begin, end: end, cancellationToken: cancellationToken);

        public void Dispatch<TAction, TParam, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TParam, TResult>> action,
            TParam parameter,
            Action begin = null,
            Action end = null,
            Func<TAction, Channel> channel = null,
            CancellationToken? cancellationToken = null)
            where TAction : IAction<TParam, TResult>
            => DispatchInner(action, parameter: parameter, channel: channel, begin: begin, end: end, cancellationToken: cancellationToken);

        public IDisposable Subscribe<TAction>(
            Func<TActionBinderSet, IActionBinder<TAction, Unit>> action,
            Action onNext,
            bool observeOnMainThread = false,
            Func<IObservable<Unit>, IObservable<Unit>> observable = null,
            Func<TAction, Channel> channel = null)
            where TAction : IAction<Unit>
            => SubscribeInner(action, (x => onNext()), observable, channel, observeOnMainThread, false);
        
        public IDisposable Subscribe<TAction, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            Action<TResult> onNext,
            bool observeOnMainThread = false,
            Func<IObservable<TResult>, IObservable<TResult>> observable = null,
            Func<TAction, Channel> channel = null,
            bool preventClone = false)
            where TAction : IAction<TResult>
            => SubscribeInner(action, onNext, observable, channel, observeOnMainThread, preventClone);

        private IDisposable SubscribeInner<TAction, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            Action<TResult> onNext,
            Func<IObservable<TResult>, IObservable<TResult>> observable = null,
            Func<TAction, Channel> channel = null,
            bool observeOnMainThread = false,
            bool preventClone = false)
            where TAction : IAction<TResult>
        {
            var act = action.Invoke(Actions);
            //채널이 생성되지 않았으면 기본 Channel클래스로 생성
            ValidateChannelId(act.Action);
            //채널 선택
            var ch = channel?.Invoke(act.Action) ?? GetDefaultChannel(act.Action);
            //액션에 해당하는 source 선택
            var source = this.GetOrAddObservableSource<TResult>(act.Action)
                .Where(x => x.channel.Ids.Intersect(ch.Ids).Any())
                .Unwrap();

            var o = observable?.Invoke(source) ?? source;

            if (observeOnMainThread)
                o = o.ObserveOn(Simplex.MainThreadScheduler);

            return o
                .Select(x => 
                {
                    var value = x;
                    try
                    {
                        //프리미티브, 열거형 타입이거나, 복사 방지 옵션이 설정되었으면 복사하지 않음
                        if (!(preventClone
                            || typeof(TResult) == typeof(Unit)
                            || typeof(TResult).IsPrimitive
                            || typeof(IEnumerable).IsAssignableFrom(x.GetType())))
                        {
                            //레퍼런스 타입의 경우 데이터 사본을 전달
                            var cloned = x.DeepClone();
                            value = cloned;
                        }
                    }
                    catch (Exception ex)
                    {
                        if (Debugger.IsAttached)
                            Debugger.Break();

                        Simplex.ExceptionSubject?.OnNext(ex);
                        Simplex.Logger?.Write(ex.StackTrace);
                    }
                    return value;
                })
                .Subscribe(onNext);
        }
        
        protected void DispatchInner<TAction, TParam, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            TParam parameter,
            Func<TAction, Channel> channel = null,
            Action begin = null,
            Action end = null,
            CancellationToken? cancellationToken = null)
            where TAction : IAction<TResult>
        {
            Simplex.MainThreadScheduler.Schedule(() => begin?.Invoke());

            void DoCompleted()
                => Simplex.MainThreadScheduler.Schedule(() => end?.Invoke());

            try
            {
                var act = action.Invoke(Actions);
                var resultObservable = act.Action.InvokeProcess(parameter)
                    .Timeout(Simplex.DefaultActionTimeout)
                    .Catch<TResult, Exception>(ex =>
                    {
                        if (act.Action.Transform(ex, out TResult result))
                        {
                            Simplex.Logger?.Write("예외가 발생되지 않고 데이터로 트랜스폼되어 배출되었습니다.");
                            return Observable.Return(result);
                        }

                        Simplex.Logger?.Write(ex.StackTrace);
                        Simplex.ExceptionSubject?.OnNext(ex);
                        return Observable.Empty<TResult>();
                    })
                    .Finally(DoCompleted);

                if (act.GetType().GetCustomAttribute<UnsubscribeAttribute>() != null)
                {
                    //더미
                    resultObservable.Subscribe();
                }
                else
                {
                    //채널이 생성되지 않았으면 기본 Channel클래스로 생성
                    ValidateChannelId(act.Action);
                    //채널 선택
                    var ch = channel?.Invoke(act.Action) ?? GetDefaultChannel(act.Action);
                    //액션에 해당하는 source 선택
                    var observer = this.GetOrAddObservableSource<TResult>(act.Action);
                    //채널 추가
                    var ob = resultObservable.Wrap(ch);

                    if (cancellationToken.HasValue)
                        ob.Subscribe(observer.OnNext, token: cancellationToken.Value);
                    else
                        ob.Subscribe(observer.OnNext);
                }
            }
            catch (Exception ex)
            {
                Simplex.ExceptionSubject?.OnNext(ex);
                Simplex.Logger?.Write(ex.StackTrace);
                DoCompleted();
            }
        }

        public DisposableStore<TActionBinderSet> ToDisposableStore(string subscriberId)
            => new DisposableStore<TActionBinderSet>(this, subscriberId);

        protected virtual Channel GetDefaultChannel<TResult>(IAction<TResult> action)
        {
            if (action is AbstractAction<TResult> act)
                return act.Default;

            throw new NotImplementedException($"{action.GetType().FullName}에서 Channel를 가져올 수 있는 방법이 구현되지 않았습니다.");
        }

        private Subject<(Channel channel, TResult result)> GetOrAddObservableSource<TResult>(IAction<TResult> action)
        {
            // Default채널의 Id를 키로 등록한다.
            var key = GetDefaultChannel(action).Id;
            return (Subject<(Channel, TResult)>)observableSources.GetOrAdd(key, new Subject<(Channel, TResult)>());
        }

        private void ValidateChannelId<TResult>(IAction<TResult> action)
        {
            //Channel 타입의 프로퍼티 필터링
            var propertyInfos = from p in action.GetType().GetProperties(BindingFlags.Instance | BindingFlags.NonPublic | BindingFlags.Public)
                                where p.PropertyType == typeof(Channel)
                                select p;

            foreach (var property in propertyInfos)
            {
                //프로퍼티 값이 생성되지 않은경우 Channel 객체 생성
                if (property.GetValue(action) is Channel channel
                    && string.IsNullOrWhiteSpace(channel.Id))
                {
                    channel.Ids = new string[] { Guid.NewGuid().ToString() };
                    if (property.CanWrite)
                    {
                        property.SetValue(action, channel);
                    }
                    else
                    {
                        //set메소드가 없는 경우, backing필드를 검색하여 fieldInfo 생성
                        var bindingFlag = BindingFlags.Instance | BindingFlags.NonPublic;
                        var fieldPropertyInfo = property.GetBackingField(action.GetType(), bindingFlag);

                        //백 필드를 못 찾으면 채널생성 skip
                        if (fieldPropertyInfo == null)
                            continue;

                        fieldPropertyInfo.SetValue(action, channel);
                    }
                }
            }
        }
    }
}
