package kr.co.tmon.simplex.actions;

import kr.co.tmon.simplex.channels.Channel;
import kr.co.tmon.simplex.channels.ChannelZip;
import kr.co.tmon.simplex.channels.IChannel;

public abstract class AbstractAction<TResult> implements IAction<TResult> {

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
	
	public AbstractAction() {
		defaultChannel = new Channel();
	}
}
