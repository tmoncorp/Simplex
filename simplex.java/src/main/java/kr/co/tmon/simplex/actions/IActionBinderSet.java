package kr.co.tmon.simplex.actions;

/**
 * 액션 클래스의 명세를 그룹으로 관리할 인터페이스
 * @author yookjy
 *
 */
public interface IActionBinderSet {
	
	/**
	 * 액션 그룹 명세를 조회 (액션 그룹이 등록되지 않았으면  신규로 등록 후 리턴)
	 * @param clazz 액션 그룹 타입
	 * @return 액션 그룹
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	<TActionBinderSet extends IActionBinderSet> TActionBinderSet getOrAdd(Class<TActionBinderSet> clazz) 
			throws InstantiationException, IllegalAccessException;
	
	/**
	 * 액션 클래스 명세를 조회 (액션 클래스의 명세가 등록되지 않았으면 신규로 등록 후 리턴)
	 * @param actionBinderKey 액션 클래스의 명세 키
	 * @param clazz 액션 클래스의 타입
	 * @return 액션 클래스 명세
	 */
	<TAction extends IAction<TParam, TResult>, TParam, TResult> 
		IActionBinder<TAction, TParam, TResult> getOrAdd(String actionBinderKey, Class<TAction> clazz);
}
