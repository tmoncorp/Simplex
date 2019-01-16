package kr.co.tmon.simplex.actions;

/**
 * 파라미터를 전달용 액션의 래핑 인터페이스
 * @author yookjy
 *
 * @param <TAction> 액션 타입
 * @param <TParam> 액션에 전달할 파라미터 타입
 * @param <TResult> 액션의 결과 타입
 */
public interface IParameterizedActionBinder<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult>
	extends IActionBinder<TAction, TResult> {
}