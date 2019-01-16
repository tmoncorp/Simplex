package kr.co.tmon.simplex.channels;

/**
 * 작업단위(액션)을 발행하거나, 구독할 때 사용될 채널 
 * @author yookjy
 *
 */
public interface IChannel {
	
	/**
	 * 채널의 고유 식별자를 조회합니다.
	 * @return 식별자
	 */
	public String getId(); 
}
