package kr.co.tmon.simplex.channels;

import java.util.UUID;

public class Channel implements IChannel {

	private String id;
	
	@Override
	public String getId() {
		return id;
	}	
	
	public Channel() {
		id = UUID.randomUUID().toString();
	}
}
