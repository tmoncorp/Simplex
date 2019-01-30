package kr.co.tmon.simplex.actions

import kr.co.tmon.simplex.channels.IChannel
import io.reactivex.Observable

/**
 * 실행할 하고 여러 채널을 통해 발행 및 결과값이 구독될 수 있는 Action의 정의
 * 실행할 하고 여러 채널을 통해 매개변수를 전달하여 발행하고, 그 결과값이 구독될 수 있는 Action의 정의
 * @param <TParam> 전달할 매개변수의 데이터 타입
 * @param <TResult> 실행 결과의 데이터 타입
*/
interface IAction<TParam, TResult> {

	/**
	 * 에러가 발생하였을때, 에러를 정상 데이터로 변환하는 메소드
	 * @param exception 에러
	 * @return 변환 결과
	 */
	fun transform(exception: Throwable): TransformedResult<TResult>

	/**
	 * 실행할 비지니스 로직을 담고 있는 메소드
	 * @param param 로직을 실행하기 위한 매개변수 데이터
	 * @return 반환할 결과 값
	 */
	fun process(param: TParam?): Observable<TResult>

	/**
	 * 다중 채널의 인터페이스를 단일 채널 인터페이스로 리턴
	 * @param channels 채널
	 */
	fun zip(vararg channels: IChannel) : IChannel
}

