package io.polarian.firework.topic

import io.polarian.firework.any
import io.polarian.firework.exceptions.InvalidReqKeyException
import io.polarian.firework.exceptions.InvalidSubKeyException
import io.polarian.firework.exceptions.NotFoundException
import io.polarian.firework.model.Subscriber
import io.polarian.firework.model.Topic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpMethod
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class DefaultTopicServiceTest {

    @InjectMocks
    private lateinit var service : DefaultTopicService

    @Mock
    private lateinit var repository : TopicRepository

    @Mock
    private lateinit var keyRepository : KeyRepository

    @Test
    fun `TEST_getTopicList_RETURN_topicList_WHEN_happy_path`() {

        val topic1 = Topic("TOPIC1", listOf("MSG #1-1", "MSG #1-2", "MSG #1-3"))
        val topic2 = Topic("TOPIC2", listOf("MSG #2-1", "MSG #2-2", "MSG #2-3"))

        Mockito.`when`(repository.getTopicList()).thenReturn(listOf(topic1, topic2))


        val result = service.getTopicList()


        assertThat(result.size).isEqualTo(2)
        assertThat(result[0]).isEqualTo(topic1)
        assertThat(result[1]).isEqualTo(topic2)
    }

    @Test
    fun `TEST_getTopicList_RETURN_emptyList_WHEN_there_are_no_topic`() {

        Mockito.`when`(repository.getTopicList()).thenReturn(emptyList())


        val result = service.getTopicList()


        assertThat(result).isEmpty()
    }

    @Test
    fun `TEST_getTopic_RETURN_topic_WHEN_happy_path`() {
        val topic = Topic("Topic1", listOf("MSG #1", "MSG #2", "MSG #3"))

        Mockito.`when`(repository.getTopic("Topic1")).thenReturn(topic)


        val result = service.getTopic("Topic1")


        assertThat(result).isEqualTo(topic)
    }

    @Test
    fun `TEST_getTopic_THROW_notFoundException_WHEN_topic_does_not_exist`() {
        Mockito.`when`(repository.getTopic("emptyTopic")).thenThrow(NotFoundException::class.java)


        val thrown = catchThrowable { service.getTopic("emptyTopic") }


        assertThat(thrown).isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `TEST_sendMessage_RETURN_true_WHEN_happy_path`() {
        //language=json
        val jsonMessage = "{\n  \"key1\":\"value1\",\n  \"key2\":\"value2\",\n  \"key3\":\"value3\"\n}"
        Mockito.`when`(repository.sendMessage("Topic1", jsonMessage)).thenReturn(true)


        val result = service.sendMessage("Topic1", jsonMessage)


        assertThat(result).isTrue()
    }

    @Test
    fun `TEST_sendMessage_RETURN_false_WHEN_topic_does_not_exist`() {
        //language=json
        val jsonMessage = "{\n  \"key1\":\"value1\",\n  \"key2\":\"value2\",\n  \"key3\":\"value3\"\n}"
        Mockito.`when`(repository.sendMessage("UNREGISTERED_TOPIC", jsonMessage)).thenReturn(false)


        val result = service.sendMessage("UNREGISTERED_TOPIC", jsonMessage)


        assertThat(result).isFalse()
    }

    @Test
    fun `TEST_setSubscriber_RETURN_subKey_WHEN_happy_path`() {
        val reqKey = "8CF4DF549391B"
        val subscriber = Subscriber("http://subscriber.polarian.io/hit/me", HttpMethod.POST)
        val subKey = UUID.randomUUID().toString()

        Mockito.`when`(keyRepository.isValidReqKey(reqKey)).thenReturn(true)
        Mockito.`when`(repository.setSubscriber("Topic1", subscriber)).thenReturn(subKey)


        val result = service.setSubscriber("Topic1", reqKey, subscriber)


        assertThat(result).isEqualTo(subKey)
    }

    @Test
    fun `TEST_setSubscriber_THROW_invalidReqKeyException_WHEN_reqKey_is_invalid`() {
        val reqKey = "8CF4DF549391B"
        val subscriber = Subscriber("http://subscriber.polarian.io/hit/me", HttpMethod.POST)


        Mockito.`when`(keyRepository.isValidReqKey(reqKey)).thenReturn(false)


        val thrown = catchThrowable { service.setSubscriber("Topic1", reqKey, subscriber) }


        assertThat(thrown).isInstanceOf(InvalidReqKeyException::class.java)
        verify(repository, never()).setSubscriber(any(), any())
    }

    @Test
    fun `TEST_setSubscriber_THROW_notFoundException_WHEN_topic_does_not_exist`() {
        val reqKey = "8CF4DF549391B"
        val subscriber = Subscriber("http://subscriber.polarian.io/hit/me", HttpMethod.POST)

        Mockito.`when`(keyRepository.isValidReqKey(reqKey)).thenReturn(true)
        Mockito.`when`(repository.setSubscriber("Topic1", subscriber)).thenThrow(NotFoundException::class.java)


        val thrown = catchThrowable { service.setSubscriber("Topic1", reqKey, subscriber) }


        assertThat(thrown).isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `TEST_unsetSubscriber_RETURN_true_WHEN_happy_path`() {
        val subKey = UUID.randomUUID().toString()

        Mockito.`when`(repository.unsetSubscriber("Topic1", subKey)).thenReturn(true)


        val result = service.unsetSubscriber("Topic1", subKey)


        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `TEST_unsetSubscriber_THROW_invalidSubKeyException_WHEN_subKey_does_not_match`() {
        val subKey = UUID.randomUUID().toString()

        Mockito.`when`(repository.unsetSubscriber("Topic1", subKey)).thenThrow(InvalidSubKeyException::class.java)


        val thrown = catchThrowable { service.unsetSubscriber("Topic1", subKey) }


        assertThat(thrown).isInstanceOf(InvalidSubKeyException::class.java)
    }

    @Test
    fun `TEST_unsetSubscriber_RETURN_false_WHEN_subscriber_does_not_exist`() {
        val subKey = UUID.randomUUID().toString()

        Mockito.`when`(repository.unsetSubscriber("Topic1", subKey)).thenReturn(false)


        val result = service.unsetSubscriber("Topic1", subKey)


        assertThat(result).isEqualTo(false)
    }
}