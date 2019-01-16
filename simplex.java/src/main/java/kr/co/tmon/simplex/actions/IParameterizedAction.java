package kr.co.tmon.simplex.actions;

import io.reactivex.Observable;

/**
 * 실행할 하고 여러 채널을 통해 매개변수를 전달하여 발행하고, 그 결과값이 구독될 수 있는 Action의 정의
 * @author yookjy
 *
 * @param <TParam> 전달할 매개변수의 데이터 타입
 * @param <TResult> 실행 결과의 데이터 타입
 */
public interface IParameterizedAction<TParam, TResult> extends IAction<TResult> {
	
	/**
	 * 실행할 비지니스 로직을 담고 있는 메소드
	 * @param param 로직을 실행하기 위한 조건 데이터
	 * @return 옵저버블 형태의 반환할 결과 값
	 */
	Observable<TResult> process(TParam param);
	
	@Override
	default Observable<TResult> process() {
		return process(null);
	}
}