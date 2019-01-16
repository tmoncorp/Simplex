package kr.co.tmon.simplex.store

import kr.co.tmon.simplex.actions.*
import kr.co.tmon.simplex.channels.IChannel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action

interface IActionStore<TActionSet> {
    /**
     * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
     * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
     * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
     * @param <TResult> 처리 결과 타입
     * @param action 단위 작업
     * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
     * @param end 액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)
     * @param channel 구독자와 연결할 채널(그룹)
     */
    fun <TAction: IAction<TResult>, TResult> dispatch(
        action: Function1<TActionSet, IActionBinder<TAction, TResult>>,
        begin: Action? = null,
        end: Action? = null,
        channel: Function1<TAction, IChannel>? = null)

    /**
     * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
     * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
     * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
     * @param <TParam> 액션에 전달할 파라미터 타입
     * @param <TResult> 처리 결과 타입
     * @param action 단위 작업
     * @param parameters 전달할 파라미터
     * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
     * @param end 액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)
     * @param channel 구독자와 연결할 채널(그룹)
     */
    fun <TAction: IParameterizedAction<TParam, TResult>, TParam, TResult> dispatch(
        action: Function1<TActionSet, IParameterizedActionBinder<TAction, TParam, TResult>>,
        parameters: TParam,
        begin: Action? = null,
        end: Action? = null,
        channel: Function1<TAction, IChannel>? = null)

    /**
     * dispach로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
     * 구독자는 Observable<T>의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
     * OnNext에 의해 배출되는 결과 데이터는 기본적으로 복사되어 전달 됩니다. (단, 원시자료형, IEnumerable 데이터는 예외)
     * 반드시 원본을 전달 받아야 하는 경우는 preventClone 파라미터를 true로 설정하여야 합니다.
     * (원본 데이터의 사용은 메모리 누수의 원인이 될 수 있습니다.)
     * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
     * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
     * @param <TAction>
     * @param <TResult> 처리 결과 타입
     * @param action 단위 작업
     * @param onNext 구독자
     * @param observeOnMainThread 메인스레드에서 파이프라인의 실행여부
     * @param observable 액션 수행 파이프라인, null 이면 추가 작업없음
     * @param channel 구독할 채널 선택, null 이면 기본 채널 사용
     * @param preventClone 수행 결과값의 원본전달 여부 (구현안함)
     * @return 구독 제거 Disposable
     */
    fun <TAction: IAction<TResult>, TResult>subscribe(
        action: Function1<TActionSet, IActionBinder<TAction, TResult>>,
        onNext: (TResult) -> Unit,
        observeOnMainThread: Boolean = false,
        observable : Function1<Observable<TResult>, Observable<TResult>>? = null,
        channel: Function1<TAction, IChannel>? = null,
        preventClone: Boolean = false
    ): Disposable?
}