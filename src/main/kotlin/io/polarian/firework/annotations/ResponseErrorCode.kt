package io.polarian.firework.annotations

import org.springframework.http.HttpStatus

annotation class ResponseErrorCode(val code: String, val msg: String)

fun getResponseErrorCode(classType: Class<out RuntimeException>): String {
    return (classType.annotations.find { it is ResponseErrorCode } as? ResponseErrorCode)?.code?:"00000"
}

fun getResponseErrorMsg(classType: Class<out RuntimeException>): String {
    return (classType.annotations.find { it is ResponseErrorCode } as? ResponseErrorCode)?.msg?:HttpStatus.INTERNAL_SERVER_ERROR.name
}