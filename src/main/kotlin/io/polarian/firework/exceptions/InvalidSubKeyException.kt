package io.polarian.firework.exceptions

import io.polarian.firework.annotations.ResponseErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseErrorCode(code = "00005", msg = "you sent the invalid subscribe key")
class InvalidSubKeyException(e: RuntimeException) : FireworkException(e)
