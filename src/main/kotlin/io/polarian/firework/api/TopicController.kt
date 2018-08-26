package io.polarian.firework.api

import io.polarian.firework.model.Subscriber
import io.polarian.firework.model.Topic
import io.polarian.firework.topic.TopicService
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("topic")
class TopicController(val topicService: TopicService) {

    @GetMapping
    fun getTopicList(): RestResponse<List<Topic>> {
        // 토픽 목록 조회
        return RestResponse(true, topicService.getTopicList())
    }

    @GetMapping("{name}")
    fun getTopic(@PathVariable name: String): RestResponse<Topic> {
        // 지정 토픽 메타 정보 조회
        return RestResponse(true, topicService.getTopic(name))
    }

    @PostMapping("{name}")
    fun postTopicMessage(@PathVariable name: String, @RequestBody body: String?): RestResponse<Boolean> {
        // 지정 토픽 메시지 발행
        return RestResponse(true, body?.let { topicService.sendMessage(name, body) } ?: false)
    }

    @PostMapping("{name}/subscriber")
    fun postTopicSubscriber(@PathVariable name: String, @RequestBody subscriberRequest: SubscriberRequest): RestResponse<String> {
        // 지정 토픽 구독자 등록
        return RestResponse(true, topicService.setSubscriber(name, subscriberRequest.reqKey, Subscriber(subscriberRequest.url, HttpMethod.valueOf(subscriberRequest.method))))
    }

    @DeleteMapping("{name}/subscriber/{subKey}")
    fun deleteTopicSubscriber(@PathVariable name: String, @PathVariable subKey: String): RestResponse<Boolean> {
        // 지정 토픽 지정 구독자 해제
        return RestResponse(true, topicService.unsetSubscriber(name, subKey))
    }
}