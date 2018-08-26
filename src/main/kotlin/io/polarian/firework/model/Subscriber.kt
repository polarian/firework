package io.polarian.firework.model

import org.springframework.http.HttpMethod

data class Subscriber(val url: String, val method: HttpMethod)