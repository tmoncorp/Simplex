package kr.co.tmon.simplex.store;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.descriptor.ISubscriptionDescriptor;
import kr.co.tmon.simplex.channels.IChannel;

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
