package kr.co.tmon.simplex.actions;

/**
 * 액션 래핑 인터페이스
 * @author yookjy
 *
 * @param <TAction> 액션 타입
 * @param <TResult> 액션의 결과값 반환 타입
 */
public interface IActionBinder<TAction extends IAction<TParam, TResult>, TParam, TResult> {

	/**
	 * 액션을 조회합니다.
	 * @return 액션
	 */
	TAction getAction();
}
