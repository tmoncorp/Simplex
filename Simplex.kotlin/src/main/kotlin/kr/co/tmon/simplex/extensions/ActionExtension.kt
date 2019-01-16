package kr.co.tmon.simplex.extensions

import kr.co.tmon.simplex.actions.*
import io.reactivex.Observable

internal fun <TParam, TResult> IAction<TResult>.toObservable(param: TParam): Observable<TResult> {
    @Suppress("UNCHECKED_CAST")
    val parameterizedAction =  this as? IParameterizedAction<TParam, TResult>
    return parameterizedAction?.process(param) ?: this.process()
}