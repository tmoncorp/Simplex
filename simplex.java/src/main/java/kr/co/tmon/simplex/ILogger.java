package kr.co.tmon.simplex;

/**
 * 단순 로그 기록용 인터페이스
 * @author yookjy
 *
 */
public interface ILogger {

	/**
	 * 문자열을 기록 합니다.
	 * @param log 기록할 문자열
	 */
	void write(String log);
	
	/**
	 * 예외 내용을 기록합니다.
	 * @param throwable 발생된 예외
	 */
	void write(Throwable throwable);
	
	/**
	 * 예외 내용을 기록합니다.
	 * 
	 * @param log 기록할 문자열
	 * @param throwable 발생된 예외
	 */
	void write(String log, Throwable throwable);
}
