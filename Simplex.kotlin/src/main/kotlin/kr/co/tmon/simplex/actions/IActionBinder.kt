package kr.co.tmon.simplex.actions


interface IActionBinder<TAction: IAction<TResult>, TResult> {
	val action: TAction 
}

interface IParameterizedActionBinder<TAction: IParameterizedAction<TParam, TResult>, TParam, TResult>
	: IActionBinder<TAction, TResult>
