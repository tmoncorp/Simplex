package kr.co.tmon.simplex.actions.descriptor;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.channels.IChannel;

/**
 * 실행된 액션의 결과를 받기 위한 조건을 설정하는 인터페이스
 * 모든 조건은 선택입력 사항입니다. 
 * @author yookjy
 *
 * @param <TAction>
 * @param <TParam>
 * @param <TResult>
 */
public interface ISubscriptionDescriptor <TAction extends IAction<TParam, TResult>, TParam, TResult>  {

	/**
	 * 결과값 처리기
	 * @param onNext
	 * @return 구독용 조건 기술자
	 */
	ISubscriptionDescriptor<TAction, TParam, TResult> onNext(Consumer<TResult> onNext);
		
	/**
	 * 결과 처리기 (전달 값 없음)
	 * @param onNext
	 * @return 구독용 조건 기술자
	 */
	ISubscriptionDescriptor<TAction, TParam, TResult> onNext(Action onNext);
	
	/**
	 * 메인스레드에서 파이프라인의 실행여부
	 * @param observeOnMainThread
	 * @return 구독용 조건 기술자
	 */
	ISubscriptionDescriptor<TAction, TParam, TResult> observeOnMainThread(boolean observeOnMainThread);
	
	/**
	 * 액션이 수행될 파이프라인
	 * @param observable
	 * @return 구독용 조건 기술자
	 */
	ISubscriptionDescriptor<TAction, TParam, TResult> observable(Function<Observable<TResult>, Observable<TResult>> observable);
	
	/**
	 * 액션을 구독할 채널을 선택합니다.
	 * 여러 채널을 선택하려면 TAction.zip() 메소드를 사용하세요.
	 * @param channel
	 * @return 구독용 조건 기술자
	 */
	ISubscriptionDescriptor<TAction, TParam, TResult> selectChannel(Function<TAction, IChannel> channel);
	
	/**
	 * 결과값의 원본 전달여부 
	 * @param preventClone
	 * @return 구독용 조건 기술자
	 */
	ISubscriptionDescriptor<TAction, TParam, TResult> preventClone(boolean preventClone);
}
