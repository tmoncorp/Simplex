package kr.co.tmon.simplex.utils;

import kr.co.tmon.simplex.channels.ChannelZip;
import kr.co.tmon.simplex.channels.IChannel;

public class ChannelUtil {
	public static IChannel zip(IChannel... channels) {
		return new ChannelZip(channels);
	}
	
	public static IChannel[] extract(IChannel channel) {
		if (channel instanceof ChannelZip) {
			return ((ChannelZip)channel).toArray();
		}
		
		IChannel[] channels = { channel };
		return channels;
	}
}
