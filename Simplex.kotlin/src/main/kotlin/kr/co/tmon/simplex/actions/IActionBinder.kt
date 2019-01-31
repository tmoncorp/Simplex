package kr.co.tmon.simplex.actions

/**
 * 액션 래핑 인터페이스
 * @param <TAction> 액션 타입
 * @param <TParam> 액션의 매개변수 타입
 * @param <TResult> 액션의 결과값 반환 타입
*/
interface IActionBinder<TAction : IAction<TParam, TResult>, TParam, TResult> {
	val action: TAction 
}
