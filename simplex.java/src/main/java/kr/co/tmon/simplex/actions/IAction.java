package kr.co.tmon.simplex.actions;

import io.reactivex.Observable;
import kr.co.tmon.simplex.channels.IChannel;

/**
 * 실행할 하고 여러 채널을 통해 발행 및 결과값이 구독될 수 있는 Action의 정의
 * 
 * @author yookjy
 *
 * @param <TResult> 실행 결과의 데이터 타입
 */
public interface IAction<TParam, TResult> {

	/**
	 * 에러가 발생하였을때, 에러를 정상 데이터로 변환하는 메소드
	 * 
	 * @param throwable 에러
	 * @return 정상 데이터로 변환된 결과 및 변환 여부
	 */
	TransformedResult<TResult> transform(Throwable throwable);

	/**
	 * 실행할 비지니스 로직을 담고 있는 메소드
	 * 
	 * @return 옵저버블 형태의 반환할 결과 값
	 */
	Observable<TResult> process(TParam param);

	/**
	 * 기본 채널을 조회
	 * 
	 * @return 채널
	 */
	IChannel getDefaultChannel();

	/**
	 * 다중 채널의 인터페이스를 단일 채널 인터페이스로 리턴
	 * 
	 * @param channels
	 * @return 채널
	 */
	IChannel zip(IChannel... channels);
}
