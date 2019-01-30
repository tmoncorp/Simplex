package kr.co.tmon.simplex.actions

import io.reactivex.Observable

abstract class AbstractUnitAction<TResult> : AbstractAction<Unit, TResult>() {
    final override fun process(param: Unit?): Observable<TResult> {
        return process()
    }

    abstract fun process(): Observable<TResult>
}