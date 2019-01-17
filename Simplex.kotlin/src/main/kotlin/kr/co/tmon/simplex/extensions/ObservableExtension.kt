package kr.co.tmon.simplex.extensions

import kr.co.tmon.simplex.channels.IChannel
import io.reactivex.Observable

fun <T> Observable<T>.wrap(channel: IChannel) : Observable<Pair<IChannel, T>> {
    return this.flatMap { Observable.just(Pair(channel, it)) }
}

fun<T: Pair<IChannel, R>, R> Observable<T>.unwrap() : Observable<R> {
    return this.flatMap { Observable.just(it.second) }
}
