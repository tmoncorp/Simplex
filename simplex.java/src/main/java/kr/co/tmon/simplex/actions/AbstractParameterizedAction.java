package kr.co.tmon.simplex.actions;

import kr.co.tmon.simplex.channels.Channel;

public abstract class AbstractParameterizedAction<TParam, TResult> extends AbstractAction<TResult> implements IParameterizedAction<TParam, TResult>{
	
	public AbstractParameterizedAction() {
		defaultChannel = new Channel();
	}
}
