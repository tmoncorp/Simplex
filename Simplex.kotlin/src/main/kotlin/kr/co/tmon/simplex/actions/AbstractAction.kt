package kr.co.tmon.simplex.actions

import kr.co.tmon.simplex.channels.*
import kr.co.tmon.simplex.extensions.*
import io.reactivex.Observable

abstract class AbstractAction<TResult> : IAction<TResult> {

	internal val default: IChannel by lazy { Channel() }

	override fun transform(exception: Throwable, result: DataHolder<TResult>): Boolean {
		result.data = null
		return false
	}

	abstract override fun process(): Observable<TResult>

	override fun zip(vararg channels: IChannel): IChannel {
		return channels.zip()
	}
}

abstract class ParametricAbstractAction<TParam, TResult> : AbstractAction<TResult>(), IParameterizedAction<TParam, TResult> {

	abstract override fun process(param: TParam?): Observable<TResult>

	override fun process(): Observable<TResult> {
		return process(null)
	}
}