package kr.co.tmon.simplex.store;

import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.IActionBinder;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.actions.descriptor.IDispatchDescriptor;
import kr.co.tmon.simplex.actions.descriptor.ISubscriptionDescriptor;

/**
 * 실행/구독용 조건 기술자를 생성 시킵니다.
 * @author yookjy
 *
 * @param <TActionBinderSet>
 */
public class DescriptorFactory<TActionBinderSet extends IActionBinderSet> {

	private TActionBinderSet actionBinderSet;
	
	DescriptorFactory(TActionBinderSet actionBinderSet) {
		this.actionBinderSet = actionBinderSet;
	}
	
	/**
	 * 실행용 조건 기술자를 생성 합니다.
	 * @param action 실행할 액션 선택기
	 * @return 실행용 조건 기술자
	 * @throws Exception
	 */
	public <TAction extends IAction<TParam, TResult>, TParam, TResult> 
		IDispatchDescriptor<TAction, TParam, TResult> to(Function<TActionBinderSet, IActionBinder<TAction, TParam, TResult>> action) throws Exception {
		
		IActionBinder<TAction, TParam, TResult> rawBinder = action.apply(actionBinderSet);
		IDispatchDescriptor<TAction, TParam, TResult> descriptor = new DispatchDescriptor<>(rawBinder.getAction());
		
		return descriptor;
	}
	
	/**
	 * 구독용 조건 기술자를 생성 합니다.
	 * @param action 구독할 액션 선택기
	 * @return 구독용 조건 기술자
	 * @throws Exception
	 */
	public <TAction extends IAction<TParam, TResult>, TParam, TResult> 
		ISubscriptionDescriptor<TAction, TParam, TResult> from(Function<TActionBinderSet, IActionBinder<TAction, TParam, TResult>> action) throws Exception {
		
		IActionBinder<TAction, TParam, TResult> rawBinder = action.apply(actionBinderSet);
		ISubscriptionDescriptor<TAction, TParam, TResult> descriptor = new SubscriptionDescriptor<>(rawBinder.getAction());
		
		return descriptor;
	}
}
