package io.polarian.firework.topic

import io.polarian.firework.model.Subscriber
import io.polarian.firework.model.Topic

interface TopicRepository {
    fun getTopicList(): List<Topic>
    fun getTopic(name: String): Topic
    fun sendMessage(name: String, message: String): Boolean
    fun setSubscriber(name: String, subscriber: Subscriber): String
    fun unsetSubscriber(name: String, subKey: String): Boolean
}
