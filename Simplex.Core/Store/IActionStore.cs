using System;
using System.Collections.Generic;
using System.Reactive;
using System.Threading;
using Tmon.Simplex.Actions;
using Tmon.Simplex.Channels;

namespace Tmon.Simplex.Store
{
    public interface IActionStore {}

    public interface IActionStore<TActionBinderSet> : IActionStore
        where TActionBinderSet : class, IActionBinderSet, new()
    {
        /// <summary>
        /// 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
        /// 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
        /// 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
        /// </summary>
        /// <typeparam name="TResult">처리 결과 타입</typeparam>
        /// <param name="action">단위 작업</param>
        /// <param name="begin">액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)</param>
        /// <param name="end">액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)</param>
        /// <param name="channel">구독자와 연결할 채널(그룹)</param>
        /// <param name="cancel">액션의 처리를 중단시킬 토큰소스</param>
        void Dispatch<TAction, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            Action begin = null,
            Action end = null,
            Func<TAction, IChannel> channel = null,
            CancellationToken? cancellationToken = null)
            where TAction : IAction<TResult>;

        /// <summary>
        /// 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
        /// 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
        /// 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
        /// </summary>
        /// <typeparam name="TParam">액션에 전달할 파라미터 타입</typeparam>
        /// <typeparam name="TResult">처리 결과 타입</typeparam>
        /// <param name="action">단위 작업</param>
        /// <param name="parameter">전달할 파라미터</param>
        /// <param name="begin">액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)</param>
        /// <param name="end">액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)</param>
        /// <param name="channel">구독자와 연결할 채널(그룹)</param>
        /// <param name="cancel">액션의 처리를 중단시킬 토큰소스</param>
        void Dispatch<TAction, TParam, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TParam, TResult>> action,
            TParam parameter,
            Action begin = null,
            Action end = null,
            Func<TAction, IChannel> channel = null,
            CancellationToken? cancellationToken = null)
            where TAction : IAction<TParam, TResult>;

        /// <summary>
        /// Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
        /// 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
        /// Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
        /// observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
        /// </summary>
        /// <param name="action">단위 작업</param>
        /// <param name="onNext">구독자</param>
        /// <param name="observeOnMainThread">메인스레드에서 파이프라인의 실행여부</param>
        /// <param name="observable">액션 수행 파이프라인</param>
        /// <param name="channel">구독할 채널</param>
        /// <returns>구독 제거용 객체</returns>
        IDisposable Subscribe<TAction>(
            Func<TActionBinderSet, IActionBinder<TAction, Unit>> action,
            Action onNext,
            bool observeOnMainThread = false,
            Func<IObservable<Unit>, IObservable<Unit>> observable = null,
            Func<TAction, IChannel> channel = null)
            where TAction : IAction<Unit>;

        /// <summary>
        /// Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
        /// 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
        /// OnNext에 의해 배출되는 결과 데이터는 기본적으로 복사되어 전달 됩니다. (단, 원시자료형, IEnumerable 데이터는 예외)
        /// 반드시 원본을 전달 받아야 하는 경우는 preventClone 파라미터를 true로 설정하여야 합니다. 
        /// (원본 데이터의 사용은 메모리 누수의 원인이 될 수 있습니다.)
        /// Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
        /// observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
        /// </summary>
        /// <typeparam name="TResult">처리 결과 타입</typeparam>
        /// <param name="action">단위 작업</param>
        /// <param name="onNext">구독자</param>
        /// <param name="observeOnMainThread">메인스레드에서 파이프라인의 실행여부</param>
        /// <param name="observable">액션 수행 파이프라인</param>
        /// <param name="channel">구독할 채널</param>
        /// <param name="preventClone">수행 결과값의 원본전달 여부</param>
        /// <returns>구독 제거용 객체</returns>
        IDisposable Subscribe<TAction, TResult>(
            Func<TActionBinderSet, IActionBinder<TAction, TResult>> action,
            Action<TResult> onNext,
            bool observeOnMainThread = false,
            Func<IObservable<TResult>, IObservable<TResult>> observable = null,
            Func<TAction, IChannel> channel = null,
            bool preventClone = false)
            where TAction : IAction<TResult>;
    }
}
