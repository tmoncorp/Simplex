package kr.co.tmon.simplex.actions;

class ParameterizedActionBinder<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> 
	extends ActionBinder<TAction, TResult>
	implements IParameterizedActionBinder<TAction, TParam, TResult> {

	public ParameterizedActionBinder(Class<TAction> clazz) throws InstantiationException, IllegalAccessException {
		super(clazz);
	}
}
