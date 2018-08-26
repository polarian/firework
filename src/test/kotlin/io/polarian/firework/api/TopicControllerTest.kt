package io.polarian.firework.api

import io.polarian.firework.annotations.getResponseErrorCode
import io.polarian.firework.annotations.getResponseErrorMsg
import io.polarian.firework.exceptions.DuplicateSubscribeException
import io.polarian.firework.exceptions.InvalidReqKeyException
import io.polarian.firework.exceptions.InvalidSubKeyException
import io.polarian.firework.exceptions.NotFoundException
import io.polarian.firework.model.Subscriber
import io.polarian.firework.model.Topic
import io.polarian.firework.topic.TopicService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class TopicControllerTest {

    @InjectMocks
    private lateinit var controller: TopicController

    @Mock
    private lateinit var service: TopicService

    private lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ControllerExceptionHandler())
                .build()
    }

    @Test
    fun `TEST_getTopicList_RETURN_response_WHEN_happy_path`() {

        val topicList = listOf(
                Topic("FIRST_TOPIC", listOf("FIRST_MESSAGE #1", "SECOND_MESSAGE #1")),
                Topic("SECOND_TOPIC", listOf("FIRST_MESSAGE #2", "SECOND_MESSAGE #2")))
        Mockito.`when`(service.getTopicList()).thenReturn(topicList)


        mockMvc.perform(get("/topic")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload").isArray)
                .andExpect(jsonPath("$.payload").isNotEmpty)
                .andExpect(jsonPath("$.payload[0].name").value("FIRST_TOPIC"))
                .andExpect(jsonPath("$.payload[0].message[0]").value("FIRST_MESSAGE #1"))
                .andExpect(jsonPath("$.payload[0].message[1]").value("SECOND_MESSAGE #1"))
                .andExpect(jsonPath("$.payload[1].name").value("SECOND_TOPIC"))
                .andExpect(jsonPath("$.payload[1].message[0]").value("FIRST_MESSAGE #2"))
                .andExpect(jsonPath("$.payload[1].message[1]").value("SECOND_MESSAGE #2"))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_getTopicList_RETURN_emptyResponse_WHEN_there_are_no_topics`() {

        Mockito.`when`(service.getTopicList()).thenReturn(emptyList())


        mockMvc.perform(get("/topic")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload").isArray)
                .andExpect(jsonPath("$.payload").isEmpty)
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_getTopic_RETURN_response_WHEN_happy_path`() {

        val topic = Topic("MY_TOPIC", listOf("FIRST_MESSAGE", "SECOND_MESSAGE"))
        Mockito.`when`(service.getTopic("MY_TOPIC")).thenReturn(topic)


        mockMvc.perform(get("/topic/MY_TOPIC")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload").isMap)
                .andExpect(jsonPath("$.payload").isNotEmpty)
                .andExpect(jsonPath("$.payload.name").value("MY_TOPIC"))
                .andExpect(jsonPath("$.payload.message[0]").value("FIRST_MESSAGE"))
                .andExpect(jsonPath("$.payload.message[1]").value("SECOND_MESSAGE"))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_getTopic_RETURN_badResponse_WHEN_requested_topic_not_found`() {

        Mockito.`when`(service.getTopic("MY_TOPIC")).thenThrow(NotFoundException::class.java)

        mockMvc.perform(get("/topic/MY_TOPIC")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.payload.code").value(getResponseErrorCode(NotFoundException::class.java)))
                .andExpect(jsonPath("$.payload.msg").value(getResponseErrorMsg(NotFoundException::class.java)))
                .andExpect(jsonPath("$.payload.method").value("GET"))
                .andExpect(jsonPath("$.payload.path").value("/topic/MY_TOPIC"))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_postTopicMessage_RETURN_response_WHEN_happy_path`() {
        //language=json
        val body = "{\n  \"msg1\":\"MY MESSAGE #1\",\n  \"msg2\":\"MY MESSAGE #2\",\n  \"msg3\":\"MY MESSAGE #3\"\n}"

        Mockito.`when`(service.sendMessage("MY_TOPIC", body)).thenReturn(true)


        mockMvc.perform(post("/topic/MY_TOPIC")
                //language=json
                .content("{\n  \"msg1\":\"MY MESSAGE #1\",\n  \"msg2\":\"MY MESSAGE #2\",\n  \"msg3\":\"MY MESSAGE #3\"\n}")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload").isBoolean)
                .andExpect(jsonPath("$.payload").value(true))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_postTopicMessage_RETURN_response_WHEN_emptyBody_received`() {
        mockMvc.perform(post("/topic/MY_TOPIC")
                //language=json
                .content("")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload").isBoolean)
                .andExpect(jsonPath("$.payload").value(false))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_postTopicSubscriber_RETURN_response_WHEN_happy_path`() {
        val subKey = UUID.randomUUID().toString()

        Mockito.`when`(service.setSubscriber("MY_TOPIC", "8CF4DF549391B", Subscriber("http://subscriber.polarian.io/hit/me", HttpMethod.POST))).thenReturn(subKey)

        mockMvc.perform(post("/topic/MY_TOPIC/subscriber")
                //language=json (256bit WEP KEY)
                .content("{\n  \"reqKey\": \"8CF4DF549391B\",\n  \"url\": \"http://subscriber.polarian.io/hit/me\",\n  \"method\": \"POST\"\n}")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload").isString)
                .andExpect(jsonPath("$.payload").value(subKey))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_postTopicSubscriber_RETURN_badResponse_WHEN_reqKey_was_invalid`() {
        Mockito.`when`(service.setSubscriber("MY_TOPIC", "INVALID_REQ_KEY", Subscriber("http://subscriber.polarian.io/hit/me", HttpMethod.POST))).thenThrow(InvalidReqKeyException::class.java)

        mockMvc.perform(post("/topic/MY_TOPIC/subscriber")
                //language=json (256bit WEP KEY)
                .content("{\n  \"reqKey\": \"INVALID_REQ_KEY\",\n  \"url\": \"http://subscriber.polarian.io/hit/me\",\n  \"method\": \"POST\"\n}")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.payload.code").value(getResponseErrorCode(InvalidReqKeyException::class.java)))
                .andExpect(jsonPath("$.payload.msg").value(getResponseErrorMsg(InvalidReqKeyException::class.java)))
                .andExpect(jsonPath("$.payload.method").value("POST"))
                .andExpect(jsonPath("$.payload.path").value("/topic/MY_TOPIC/subscriber"))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_postTopicSubscriber_RETURN_badResponse_WHEN_url_was_already_subscribed`() {
        Mockito.`when`(service.setSubscriber("MY_TOPIC", "8CF4DF549391B", Subscriber("http://subscriber.polarian.io/hit/me", HttpMethod.POST))).thenThrow(DuplicateSubscribeException::class.java)

        mockMvc.perform(post("/topic/MY_TOPIC/subscriber")
                //language=json (256bit WEP KEY)
                .content("{\n  \"reqKey\": \"8CF4DF549391B\",\n  \"url\": \"http://subscriber.polarian.io/hit/me\",\n  \"method\": \"POST\"\n}")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.payload.code").value(getResponseErrorCode(DuplicateSubscribeException::class.java)))
                .andExpect(jsonPath("$.payload.msg").value(getResponseErrorMsg(DuplicateSubscribeException::class.java)))
                .andExpect(jsonPath("$.payload.method").value("POST"))
                .andExpect(jsonPath("$.payload.path").value("/topic/MY_TOPIC/subscriber"))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_deleteTopicSubscriber_RETURN_response_WHEN_happy_path`() {
        val subKey = UUID.randomUUID().toString()

        Mockito.`when`(service.unsetSubscriber("MY_TOPIC", subKey)).thenReturn(true)

        mockMvc.perform(delete("/topic/MY_TOPIC/subscriber/$subKey")
                //language=json (256bit WEP KEY)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload").isBoolean)
                .andExpect(jsonPath("$.payload").value(true))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_deleteTopicSubscriber_RETURN_badResponse_WHEN_no_subscriber_exists_for_subKey`() {
        val subKey = UUID.randomUUID().toString()

        Mockito.`when`(service.unsetSubscriber("MY_TOPIC", subKey)).thenThrow(InvalidSubKeyException::class.java)

        mockMvc.perform(delete("/topic/MY_TOPIC/subscriber/$subKey")
                //language=json (256bit WEP KEY)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.payload.code").value(getResponseErrorCode(InvalidSubKeyException::class.java)))
                .andExpect(jsonPath("$.payload.msg").value(getResponseErrorMsg(InvalidSubKeyException::class.java)))
                .andExpect(jsonPath("$.payload.method").value("DELETE"))
                .andExpect(jsonPath("$.payload.path").value("/topic/MY_TOPIC/subscriber/$subKey"))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }

    @Test
    fun `TEST_controllerExceptionAdvice_RETURN_badResponse_WHEN_insufficient_arguments`() {

        mockMvc.perform(post("/topic/MY_TOPIC/subscriber")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError)
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.payload.code").value(getResponseErrorCode(RuntimeException::class.java)))
                .andExpect(jsonPath("$.payload.msg").value(getResponseErrorMsg(RuntimeException::class.java)))
                .andExpect(jsonPath("$.payload.method").value("POST"))
                .andExpect(jsonPath("$.payload.path").value("/topic/MY_TOPIC/subscriber"))
                .andExpect(jsonPath("$.timestamp").isNumber)
    }
}