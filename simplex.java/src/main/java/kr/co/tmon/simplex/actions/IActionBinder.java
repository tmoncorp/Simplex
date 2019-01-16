package kr.co.tmon.simplex.actions;

public interface IActionBinder<TAction extends IAction<TResult>, TResult> {

	TAction getAction();
}
