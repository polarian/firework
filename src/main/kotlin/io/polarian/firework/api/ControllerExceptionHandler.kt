package io.polarian.firework.api

import io.polarian.firework.exceptions.FireworkException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
@ResponseBody
class ControllerExceptionHandler {

    @ExceptionHandler(FireworkException::class)
    fun fireworkExceptionHandler(e: FireworkException, request: HttpServletRequest): ResponseEntity<RestResponse<ErrorMessage>> {

        return ResponseEntity(
                RestResponse(
                        false,
                        ErrorMessage(e.getErrorCode(), e.getErrorMsg(), request.method.toUpperCase(), request.pathInfo)),
                e.getResponseStatus())
    }

    @ExceptionHandler(RuntimeException::class)
    fun runtimeExceptionHandler(e: RuntimeException, request: HttpServletRequest): ResponseEntity<RestResponse<ErrorMessage>> {
        e.printStackTrace()
        return ResponseEntity(
                RestResponse(
                        false,
                        ErrorMessage("00000", HttpStatus.INTERNAL_SERVER_ERROR.name, request.method.toUpperCase(), request.pathInfo)),
                HttpStatus.INTERNAL_SERVER_ERROR)
    }
}