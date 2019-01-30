package kr.co.tmon.simplex.store;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.IActionBinder;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.actions.descriptor.IDispatchDescriptor;
import kr.co.tmon.simplex.actions.descriptor.ISubscriptionDescriptor;
import kr.co.tmon.simplex.channels.IChannel;

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

/**
 * 실행용 조건 기술자의 구현체(내부 생성용으로 외부에 노출되지 않습니다.)
 * @author yookjy
 *
 * @param <TAction>
 * @param <TParam>
 * @param <TResult>
 */
class DispatchDescriptor<TAction extends IAction<TParam, TResult>, TParam, TResult> 
	implements IDispatchDescriptor<TAction, TParam, TResult> {

	private TParam parameter;
	
	private TAction action;
	
	private Action begin;
	
	private Action end;
	
	private Function<TAction, IChannel> channel;
	
	TAction getAction() {
		return action;
	}
	
	TParam getParameter() {
		return parameter;
	}
	
	Action getBegin() {
		return begin;
	}
	
	Action getEnd() {
		return end;
	}
	
	Function<TAction, IChannel> getChannel() {
		return channel;
	}
	
	public DispatchDescriptor(TAction action) {
		this.action = action;
	}
	
	@Override
	public IDispatchDescriptor<TAction, TParam, TResult> setParameter(TParam param) {
		this.parameter = param;
		return this;
	}
		
	@Override
	public IDispatchDescriptor<TAction, TParam, TResult> onBegin(Action begin) {
		this.begin = begin;
		return this;
	}
	
	@Override
	public IDispatchDescriptor<TAction, TParam, TResult> onEnd(Action end) {
		this.end = end;
		return this;
	}

	@Override
	public IDispatchDescriptor<TAction, TParam, TResult> selectChannel(Function<TAction, IChannel> channel) {
		this.channel = channel;
		return this;
	}
	
}

/**
 * 구독용 조건 기술자의 구현체(내부 생성용으로 외부에 노출되지 않습니다.)
 * @author yookjy
 *
 * @param <TAction>
 * @param <TParam>
 * @param <TResult>
 */
class SubscriptionDescriptor<TAction extends IAction<TParam, TResult>, TParam, TResult> 
	implements ISubscriptionDescriptor<TAction, TParam, TResult> {

	private TAction action;
	
	private Consumer<TResult> onNext;
	
	private boolean observeOnMainThread;
	
	private Function<Observable<TResult>, Observable<TResult>> observable;
	
	private Function<TAction, IChannel> channel;
	
	private boolean preventClone;
	
	TAction getAction() {
		return action;
	}
	
	Consumer<TResult> getOnNext() {
		return onNext;
	}
	
	boolean getObserveOnMainThread() {
		return observeOnMainThread;
	}
	
	Function<Observable<TResult>, Observable<TResult>> getObservable() {
		return observable;
	}
	
	Function<TAction, IChannel> getChannel() {
		return channel;
	}
	
	boolean getPreventClone() {
		return preventClone;
	}
	
	public SubscriptionDescriptor(TAction action) {
		this.action = action;
	}
	
	@Override
	public ISubscriptionDescriptor<TAction, TParam, TResult> selectChannel(Function<TAction, IChannel> channel) {
		this.channel = channel;
		return this;
	}

	@Override
	public ISubscriptionDescriptor<TAction, TParam, TResult> onNext(Consumer<TResult> onNext) {
		this.onNext = onNext;
		return this;
	}
	
	@Override
	public ISubscriptionDescriptor<TAction, TParam, TResult> onNext(Action onNext) {
		
		this.onNext = x -> onNext.run();
		return this;
	}

	@Override
	public ISubscriptionDescriptor<TAction, TParam, TResult> observeOnMainThread(boolean observeOnMainThread) {
		this.observeOnMainThread = observeOnMainThread;
		return this;
	}

	@Override
	public ISubscriptionDescriptor<TAction, TParam, TResult> observable(
			Function<Observable<TResult>, Observable<TResult>> observable) {
		this.observable = observable;
		return this;
	}

	@Override
	public ISubscriptionDescriptor<TAction, TParam, TResult> preventClone(boolean preventClone) {
		this.preventClone = preventClone;
		return this;
	}
}
