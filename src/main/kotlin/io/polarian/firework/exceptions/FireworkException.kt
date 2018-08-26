package io.polarian.firework.exceptions

import io.polarian.firework.annotations.ResponseErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseErrorCode(code = "99999", msg = "unknown error")
open class FireworkException(e: RuntimeException) : RuntimeException(e) {

    fun getErrorCode(): String {
        return this.javaClass.getAnnotation(ResponseErrorCode::class.java).code
    }

    fun getErrorMsg(): String {
        return this.javaClass.getAnnotation(ResponseErrorCode::class.java).msg
    }

    fun getResponseStatus(): HttpStatus {
        return this.javaClass.getAnnotation(ResponseStatus::class.java).value
    }
}
