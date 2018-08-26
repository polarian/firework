package io.polarian.firework.exceptions

import io.polarian.firework.annotations.ResponseErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseErrorCode(code = "00003", msg = "you sent the invalid request key")
class InvalidReqKeyException(e: RuntimeException) : FireworkException(e)
