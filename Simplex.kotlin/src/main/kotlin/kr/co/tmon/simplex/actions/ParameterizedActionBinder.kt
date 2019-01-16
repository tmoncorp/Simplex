package kr.co.tmon.simplex.actions

class ParameterizedActionBinder<TAction: IParameterizedAction<TParam, TResult>, TParam, TResult>(factory: () -> TAction)
    : IParameterizedActionBinder<TAction, TParam, TResult> {

    companion object {
        inline operator fun <reified TAction: IParameterizedAction<TParam, TResult>, TParam, TResult> invoke()
                = ParameterizedActionBinder<TAction, TParam, TResult> {
            TAction::class.java.newInstance()
        }
    }

    override val action: TAction = factory()
}