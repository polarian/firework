package io.polarian.firework.topic

import io.polarian.firework.model.Subscriber
import io.polarian.firework.model.Topic
import org.springframework.stereotype.Repository

@Repository
class DefaultTopicRepository : TopicRepository {
    override fun getTopicList(): List<Topic> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTopic(name: String): Topic {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessage(name: String, message: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSubscriber(name: String, subscriber: Subscriber): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsetSubscriber(name: String, subKey: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
