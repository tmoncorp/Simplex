package kr.co.tmon.simplex.actions;

public interface IParameterizedActionBinder<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult>
	extends IActionBinder<TAction, TResult> {
}