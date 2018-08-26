package io.polarian.firework.topic

import io.polarian.firework.model.Message
import io.polarian.firework.model.Subscriber
import io.polarian.firework.model.Topic

interface TopicService {
    fun getTopicList(): List<Topic>
    fun getTopic(name: String): Topic
    fun sendMessage(name: String, message: Message): Boolean
    fun setSubscriber(name: String, reqKey: String, subscriber: Subscriber): String
    fun unsetSubscriber(name: String, subKey: String): Boolean
}
