package kr.co.tmon.simplex.actions;

/**
 * 액션 래핑 클래스를 그룹화형 관리할 인터페이스
 * @author yookjy
 *
 */
public interface IActionBinderSet {
	
	/**
	 * 액션 래핑 클래스를 조회 (등록된 액션 래핑 클래스가 존재하지 않으면  신규로 등록 함)
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	<TActionBinderSet extends IActionBinderSet> TActionBinderSet getOrAdd(Class<TActionBinderSet> clazz) 
			throws InstantiationException, IllegalAccessException;
}
