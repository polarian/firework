package io.polarian.firework.exceptions

import io.polarian.firework.annotations.ResponseErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseErrorCode(code = "00004", msg = "the requested url was already subscribed")
class DuplicateSubscribeException(e: RuntimeException) : FireworkException(e)
