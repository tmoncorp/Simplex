package kr.co.tmon.simplex.actions;

import kr.co.tmon.simplex.channels.Channel;
import kr.co.tmon.simplex.channels.ChannelZip;
import kr.co.tmon.simplex.channels.IChannel;
import kr.co.tmon.simplex.reactivex.Unit;

public abstract class AbstractUnitAction<TResult> implements IAction<Unit, TResult> {

	protected IChannel defaultChannel;
	
	@Override
	public IChannel getDefaultChannel() {
		return defaultChannel;
	}
	
	@Override
	public TransformedResult<TResult> transform(Throwable throwable) {
		return new TransformedResult<TResult>(false, null);
	}

	@Override
	public IChannel zip(IChannel...channels) {
		IChannel channel = new ChannelZip(channels);
		return channel;
	}
	
	public AbstractUnitAction() {
		defaultChannel = new Channel();
	}
}
