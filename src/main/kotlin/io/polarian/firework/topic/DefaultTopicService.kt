package io.polarian.firework.topic

import io.polarian.firework.exceptions.InvalidReqKeyException
import io.polarian.firework.model.Message
import io.polarian.firework.model.Subscriber
import io.polarian.firework.model.Topic
import org.springframework.stereotype.Service

@Service
class DefaultTopicService(val topicRepository: TopicRepository,
                          val keyRepository: KeyRepository) : TopicService {
    override fun getTopicList(): List<Topic> {
        return topicRepository.getTopicList()
    }

    override fun getTopic(name: String): Topic {
        return topicRepository.getTopic(name)
    }

    override fun sendMessage(name: String, message: Message): Boolean {
        return topicRepository.sendMessage(name, message)
    }

    override fun setSubscriber(name: String, reqKey: String, subscriber: Subscriber): String {
        if(!keyRepository.isValidReqKey(reqKey)) throw InvalidReqKeyException(RuntimeException())

        return topicRepository.setSubscriber(name, subscriber)
    }

    override fun unsetSubscriber(name: String, subKey: String): Boolean {
        return topicRepository.unsetSubscriber(name, subKey)
    }
}
