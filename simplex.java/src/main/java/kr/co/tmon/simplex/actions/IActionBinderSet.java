package kr.co.tmon.simplex.actions;

public interface IActionBinderSet {
	
	<TActionBinderSet extends IActionBinderSet> TActionBinderSet getOrAdd(Class<TActionBinderSet> clazz) 
			throws InstantiationException, IllegalAccessException;
}
