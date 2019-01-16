package kr.co.tmon.simplex.actions

class ActionBinder<TAction: IAction<TResult>, TResult>(factory: () -> TAction)
	: IActionBinder<TAction, TResult> {

	companion object {
		inline operator fun <reified TAction: IAction<TResult>, TResult> invoke()
				= ActionBinder<TAction, TResult> {
			TAction::class.java.newInstance()
		}
	}
	override val action: TAction = factory()
}

