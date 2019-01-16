package kr.co.tmon.simplex.store;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.IParameterizedAction;
import kr.co.tmon.simplex.actions.IActionBinder;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.actions.IParameterizedActionBinder;
import kr.co.tmon.simplex.channels.IChannel;
import kr.co.tmon.simplex.reactivex.Unit;

public interface IActionStore<TActionBinderSet extends IActionBinderSet> {

	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
     * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
     * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param parameter 전달할 파라미터
	 * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
	 * @param end 액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)
	 * @param channel 구독자와 연결할 채널(그룹)
	 */
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin,
			Action end,
			Function<TAction, IChannel> channel);
	
	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
     * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
     * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param parameter 전달할 파라미터
	 * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
	 */
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin);
	
	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
     * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
     * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param parameter 전달할 파라미터
	 * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
	 * @param end 액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)
	 */
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin,
			Action end);

	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
     * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
     * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param parameter 전달할 파라미터
	 * @param channel 구독자와 연결할 채널(그룹)
	 */
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Function<TAction, IChannel> channel);
		
	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
	 * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
	 * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
	 * @param end 액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)
	 * @param channel 구독자와 연결할 채널(그룹)
	 */
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin,
			Action end,
			Function<TAction, IChannel> channel);
	
	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
	 * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
	 * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
	 */
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin);

	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
	 * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
	 * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param begin 액션 처리 직전에 수행 (메인 스레드에서 수행됩니다.)
	 * @param end 액션 처리 직후에 수행 (메인 스레드에서 수행됩니다.)
	 */
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin,
			Action end);

	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
	 * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
	 * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param action 단위 작업
	 * @param channel 구독자와 연결할 채널(그룹)
	 */
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Function<TAction, IChannel> channel);

	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @param observable 액션 수행 파이프라인
	 * @param channel 구독할 채널
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread,
			Function<Observable<Unit>, Observable<Unit>> observable,
			Function<TAction, IChannel> channel);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observable 액션 수행 파이프라인
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable);
		
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @param observable 액션 수행 파이프라인
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread,
			Function<Observable<Unit>, Observable<Unit>> observable);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param channel 구독할 채널
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;Unit&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observable 액션 수행 파이프라인
	 * @param channel 구독할 채널
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable,
			Function<TAction, IChannel> channel);

	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @param observable 액션 수행 파이프라인
	 * @param channel 구독할 채널
	 * @param preventClone 결과값의 원본 전달여부 
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부 
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observable 액션 수행 파이프라인 
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable);

	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @param preventClone 결과값의 원본 전달여부 
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			boolean preventClone);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @param observable 액션 수행 파이프라인
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @param observable 액션 수행 파이프라인
	 * @param channel 구독할 채널 
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param channel 구독할 채널
	 * @param observerOnMainThread 메인스레드에서 파이프라인의 실행여부
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread);

	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observable 액션 수행 파이프라인
	 * @param channel 구독할 채널
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel);
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param action 단위 작업
	 * @param onNext 구독자
	 * @param observable 액션 수행 파이프라인
	 * @param channel 구독할 채널
	 * @param preventClone 결과값의 원본 전달여부 
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone);
}


