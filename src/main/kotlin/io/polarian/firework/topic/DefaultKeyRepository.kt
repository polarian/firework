package io.polarian.firework.topic

import org.springframework.stereotype.Repository

@Repository
class DefaultKeyRepository : KeyRepository {
    override fun isValidReqKey(reqKey: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}