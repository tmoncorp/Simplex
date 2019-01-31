package kr.co.tmon.simplex.actions

class ActionBinder<TAction : IAction<TParam, TResult>, TParam, TResult>(factory: () -> TAction)
	: IActionBinder<TAction, TParam, TResult> {

	companion object {
		inline operator fun <reified TAction : IAction<TParam, TResult>, TParam, TResult> invoke()
				= ActionBinder<TAction, TParam, TResult> {
			TAction::class.java.newInstance()
		}
	}

	override val action: TAction = factory()
}
