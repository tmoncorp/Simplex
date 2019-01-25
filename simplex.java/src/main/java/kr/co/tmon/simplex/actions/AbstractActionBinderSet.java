package kr.co.tmon.simplex.actions;

import java.util.ArrayList;
import java.util.Hashtable;

public abstract class AbstractActionBinderSet implements IActionBinderSet {

	private Hashtable<String, IActionBinder<?, ?, ?>> actionBinders;
	
	private ArrayList<IActionBinderSet> actionBinderSets;
	
	public AbstractActionBinderSet() {
		this.actionBinders = new Hashtable<String, IActionBinder<?, ?, ?>>();
		this.actionBinderSets = new ArrayList<IActionBinderSet>();
	}
	
	@SuppressWarnings("unchecked")
	protected <TAction extends IAction<TParam, TResult>, TParam, TResult> 
		IActionBinder<TAction, TParam, TResult> getOrAdd(String actionBinderKey, Class<TAction> clazz) {	
		if (actionBinderKey == null || "".equals(actionBinderKey)) {
			throw new NullPointerException("actionBinderKey는 필수 입력값 입니다.");
		}
		
		if (!actionBinders.containsKey(actionBinderKey)) {
			try {
				actionBinders.put(actionBinderKey, new ActionBinder<>(clazz));
			} catch (Exception e) {
				throw new NullPointerException("actionBinder는 필수 입력값입니다.");
			}
		}
		
		return (IActionBinder<TAction, TParam, TResult>) actionBinders.get(actionBinderKey);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public <TActionBinderSet extends IActionBinderSet> TActionBinderSet getOrAdd(Class<TActionBinderSet> clazz) 
			throws InstantiationException, IllegalAccessException {
		
		String typeName = clazz.getTypeName();
		TActionBinderSet actionBinderSet = null;
		
		for(IActionBinderSet set : actionBinderSets) {
			actionBinderSet = (TActionBinderSet)set;
			if (set.getClass().getTypeName().equals(typeName)) {
				break;
			}
		}
		
		if (actionBinderSet == null) {
			actionBinderSet = clazz.newInstance();
			actionBinderSets.add(actionBinderSet);
		}
		
		return actionBinderSet;
	}

	
	
}
