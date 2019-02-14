using System;
using System.Collections.Concurrent;
using System.Reactive;
using System.Reactive.Disposables;
using System.Threading;
using Tmon.Simplex.Actions;
using Tmon.Simplex.Channels;

namespace Tmon.Simplex.Store
{
    public class DisposableStore<TActionBinderSet> : IActionStore<TActionBinderSet>, IDisposable
        where TActionBinderSet : class, IActionBinderSet, new()
    {
        private IActionStore<TActionBinderSet> Store { get; }

        private readonly string subscriberId;

        private static readonly ConcurrentDictionary<string, CompositeDisposable> managedDisposables 
            = new ConcurrentDictionary<string, CompositeDisposable>();

        private DisposableStore() { }

        /// <summary>
        /// 스토어에 식별자를 부여하여 관리되는 스토어를 생성합니다.
        /// </summary>
        /// <param name="store">스토어 객체</param>
        /// <param name="subscriberId">부여할 식별자</param>
        public DisposableStore(IActionStore<TActionBinderSet> store, string subscriberId)
        {
            this.Store = store;
            this.subscriberId = subscriberId;
        }

        public void Dispatch<TAction, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            Action begin = null,
            Action end = null,
            Func<TAction, Channel> channel = null,
            CancellationToken? cancellationToken = null)
            where TAction : IAction<TResult>
            => Store.Dispatch(action, begin, end, channel, cancellationToken);

        public void Dispatch<TAction, TParam, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TParam, TResult>> action,
            TParam parameter,
            Action begin = null,
            Action end = null,
            Func<TAction, Channel> channel = null,
            CancellationToken? cancellationToken = null)
            where TAction : IAction<TParam, TResult>
            => Store.Dispatch(action, parameter, begin, end, channel, cancellationToken);

        public IDisposable Subscribe<TAction>(
            Func<TActionBinderSet, IActionBinder<TAction, Unit>> action,
            Action onNext,
            bool observeOnMainThread = false,
            Func<IObservable<Unit>, IObservable<Unit>> observable = null,
            Func<TAction, Channel> channel = null)
            where TAction : IAction<Unit>
            => AddDisposable(Store.Subscribe(action, onNext, observeOnMainThread, observable, channel));
       
        public IDisposable Subscribe<TAction, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            Action<TResult> onNext,
            bool observeOnMainThread = false,
            Func<IObservable<TResult>, IObservable<TResult>> observable = null,
            Func<TAction, Channel> channel = null,
            bool preventClone = false)
            where TAction : IAction<TResult>
            => AddDisposable(Store.Subscribe(action, onNext, observeOnMainThread, observable, channel, preventClone));
        
        /// <summary>
        /// 구독이 등록될때 발생하는 삭제패턴(IDisposable)객체를 관리 목록에 등록합니다.
        /// 관리목록은 모든 관리형 스토어에 공유되며, 삭제 패턴객체는 관리 스토어 식별자 별로 관리됩니다.
        /// </summary>
        /// <param name="disposables">삭제패턴 객체</param>
        /// <returns>삭제패턴 객체</returns>
        private IDisposable AddDisposable(IDisposable disposable)
        {
            if (disposable == null)
                return null;

            managedDisposables.AddOrUpdate(
                key: subscriberId,
                addValueFactory: key =>
                {
                    return new CompositeDisposable(disposable);
                },
                updateValueFactory: (key, list) =>
                {
                    if (!list.IsDisposed)
                    {
                        list.Add(disposable);
                    }
                    return list;
                });
            
            return disposable;
        }
        
        public void Dispose()
        {
            //관리형 스토어별로 등록된 구독을 모두 해제 합니다.
            if (managedDisposables.TryRemove(subscriberId, out CompositeDisposable value) && value != null)
            {
                if (!value.IsDisposed)
                    value.Dispose();
            }
        }
    }
}
