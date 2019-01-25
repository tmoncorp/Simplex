package kr.co.tmon.simplex.store;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.actions.descriptor.IDispatchDescriptor;
import kr.co.tmon.simplex.actions.descriptor.ISubscriptionDescriptor;

public interface IActionStore<TActionBinderSet extends IActionBinderSet> {

	/**
	 * 액션(단위 작업)의 처리를 요청합니다. 이 요청에 대한 처리는 비동기로 수행됩니다.
     * 액션이 수행되면 결과를 발행시키며, 결과는 구독자에게 전달이 됩니다.
     * 이때 특정 구독자에게만 결과를 방행시키고 싶으면 Channel을 구현하여 통신하여야 합니다.
	 * @param descriptor 발행할 액션에 대한 조건 기술자
	 */
	<TAction extends IAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<DescriptorFactory<TActionBinderSet>, IDispatchDescriptor<TAction, TParam, TResult>> descriptor);
	
	
	/**
	 * Dispatch로 처리가 요청된 액션의 결과를 구독하기 위한 구독자를 등록합니다.
	 * 구독자는 IObservable&lt;TResult&gt;의 구현체이어야 하며, Simplex에서는 OnNext만 사용합니다.
	 * Simplex는 에러가 발생되면 구독자의 OnError로 배출하지 않고 Simplex.DefaultExceptionHandler를 통해 배출 합니다.
	 * observable파라미터를 통해 구독 결과에 대한 필터를 사용할 수 있습니다.
	 * @param descriptor 구독할 액션에 대한 조건 기술자
	 * @return 구독 제거용 객체
	 */
	<TAction extends IAction<TParam, TResult>, TParam, TResult> Disposable subscribe(
			Function<DescriptorFactory<TActionBinderSet>, ISubscriptionDescriptor<TAction, TParam, TResult>> descriptor);
}


