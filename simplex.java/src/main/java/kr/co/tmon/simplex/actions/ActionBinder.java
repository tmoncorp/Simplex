package kr.co.tmon.simplex.actions;

class ActionBinder<TAction extends IAction<TParam, TResult>, TParam, TResult> implements IActionBinder<TAction, TParam, TResult> {

	private TAction action;
	
	public TAction getAction() {
		return action;
	}
	
//	public ActionBinder(TAction action) {
//		this.action = action;
//	}
	
	public ActionBinder(Class<TAction> clazz) throws InstantiationException, IllegalAccessException {
		action = clazz.newInstance();
	}
}
