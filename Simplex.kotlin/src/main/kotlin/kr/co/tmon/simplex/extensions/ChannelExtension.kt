package kr.co.tmon.simplex.extensions

import kr.co.tmon.simplex.channels.*

fun Array<out IChannel>.zip() : IChannel {
    return ChannelZip(this)
}

fun IChannel.extract() : Array<out IChannel> {
    return when (this) {
        is ChannelZip -> this.channels
        else -> arrayOf(this)
    }
}

fun IChannel.contains(value: IChannel) : Boolean {
    return this.extract().intersect(value.extract().asIterable()).any()
}