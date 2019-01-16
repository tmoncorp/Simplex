package kr.co.tmon.simplex.actions;

class ActionBinder<TAction extends IAction<TResult>, TResult> implements IActionBinder<TAction, TResult> {

	private TAction action;
	
	public TAction getAction() {
		return action;
	}
	
	public ActionBinder(Class<TAction> clazz) throws InstantiationException, IllegalAccessException {
		action = clazz.newInstance();
	}
}
