package io.polarian.firework.topic

interface KeyRepository {
    fun isValidReqKey(reqKey: String): Boolean
}
