package kr.co.tmon.simplex.channels;

import java.util.UUID;

public class ChannelZip implements IChannel {

	private String id;
	
	private IChannel[] channels;
	
	@Override
	public String getId() {
		return id;
	}
	
	IChannel[] getChannels()	{
		return channels;
	}
	
	private ChannelZip() {
		id = UUID.randomUUID().toString();
	}
	
	public ChannelZip(IChannel[] channels) {
		this();
		this.channels = channels;
	}
	
	public IChannel[] toArray() {
		return channels;
	}
}
