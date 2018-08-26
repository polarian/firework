package io.polarian.firework.exceptions

import io.polarian.firework.annotations.ResponseErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseErrorCode(code = "00002", msg = "request does not exists")
class NotFoundException(e: RuntimeException) : FireworkException(e)
