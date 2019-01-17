package kr.co.tmon.simplex.actions

import kr.co.tmon.simplex.channels.*
import kr.co.tmon.simplex.extensions.*
import io.reactivex.Observable

abstract class AbstractAction<TResult> : IAction<TResult> {

	internal val default: IChannel by lazy { Channel() }

	override fun transform(exception: Throwable) : TransformedResult<TResult> {
        return TransformedResult(false, null)
	}

	abstract override fun process() : Observable<TResult>

	override fun zip(vararg channels: IChannel) : IChannel {
		return channels.zip()
	}
}
