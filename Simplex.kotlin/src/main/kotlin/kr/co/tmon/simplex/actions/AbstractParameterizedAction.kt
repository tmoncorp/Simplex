package kr.co.tmon.simplex.actions

import io.reactivex.Observable

abstract class AbstractParameterizedAction<TParam, TResult>
    : AbstractAction<TResult>(), IParameterizedAction<TParam, TResult> {

    abstract override fun process(param: TParam?) : Observable<TResult>

    override fun process() : Observable<TResult> {
        return process(null)
    }
}