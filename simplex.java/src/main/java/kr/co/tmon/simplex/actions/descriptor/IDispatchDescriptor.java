package kr.co.tmon.simplex.actions.descriptor;

import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.channels.IChannel;

/**
 * 실행할 액션에 대한 조건을 설정하는 인터페이스입니다.
 * 모든 조건은 선택입력 사항입니다. 
 * @author yookjy
 *
 * @param <TAction> 실행할 액션
 * @param <TParam> 액션에 전할할 매개변수 타입
 * @param <TResult> 액션을 실행시키고 반환받을 타입
 */
public interface IDispatchDescriptor<TAction extends IAction<TParam, TResult>, TParam, TResult> {
	
	/**
	 * 액션에 전달할 값
	 * @param param 
	 * @return 실행용 조건 기술자
	 */
	IDispatchDescriptor<TAction, TParam, TResult> setParameter(TParam param);
	
	/**
	 * 액션 실행 직전에 수행할 처리기 (메인 스레드에서 동작)
	 * @param begin
	 * @return 실행용 조건 기술자
	 */
	IDispatchDescriptor<TAction, TParam, TResult> onBegin(Action begin);
	
	/**
	 * 액션 실행 직후에 수행할 처리기 (메인 스레드에서 동작)
	 * @param end
	 * @return 실행용 조건 기술자
	 */
	IDispatchDescriptor<TAction, TParam, TResult> onEnd(Action end);
	
	/**
	 * 액션을 실행할 채널을 선택합니다.
	 * 여러 채널을 선택하려면 TAction.zip() 메소드를 사용하세요.
	 * @param channel
	 * @return 실행용 조건 기술자
	 */
	IDispatchDescriptor<TAction, TParam, TResult> selectChannel(Function<TAction, IChannel> channel);
}
