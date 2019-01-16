package kr.co.tmon.simplex.actions

import kr.co.tmon.simplex.channels.IChannel
import io.reactivex.Observable

/**
 * 실행할 하고 여러 채널을 통해 발행 및 결과값이 구독될 수 있는 Action의 정의
 */
interface IAction<TResult> {
	/// <summary>
	/// 에러가 발생하였을때, 에러를 정상 데이터로 변환하는 메소드
	/// </summary>
	/// <param name="exception">에러</param>
	/// <param name="result">정상 데이터로 변환된 결과</param>
	/// <returns>변환 성공 유무</returns>
	fun transform(exception: Throwable, result: DataHolder<TResult>): Boolean

	/// <summary>
	/// 실행할 비지니스 로직을 담고 있는 메소드
	/// </summary>
	/// <returns>반환할 결과 값</returns>
	fun process(): Observable<TResult>
	
	/// <summary>
	/// 다중 채널의 인터페이스를 단일 채널 인터페이스로 리턴
	/// </summary>
	/// <param name="channels"></param>
	/// <returns></returns>
	fun zip(vararg channels: IChannel): IChannel
}

