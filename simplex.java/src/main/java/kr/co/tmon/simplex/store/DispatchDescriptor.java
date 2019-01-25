package kr.co.tmon.simplex.store;

import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.descriptor.IDispatchDescriptor;
import kr.co.tmon.simplex.channels.IChannel;

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
