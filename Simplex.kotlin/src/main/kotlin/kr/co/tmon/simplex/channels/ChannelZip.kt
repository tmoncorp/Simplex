package kr.co.tmon.simplex.channels

import java.util.*

internal class ChannelZip(internal val channels: Array<out IChannel>) : IChannel {
    override val id : String = UUID.randomUUID().toString()
}