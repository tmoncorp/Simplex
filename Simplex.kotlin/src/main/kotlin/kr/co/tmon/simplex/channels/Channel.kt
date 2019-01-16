package kr.co.tmon.simplex.channels

import java.util.UUID

class Channel : IChannel{
    override val id: String = UUID.randomUUID().toString()

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is IChannel -> this.id == other.id
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

