package kr.co.tmon.simplex.actions;

import kr.co.tmon.simplex.channels.IChannel;

public interface IRawAction {
	
	IChannel getDefaultChannel();
	
	IChannel zip(IChannel... channels);
}
