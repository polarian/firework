package io.polarian.firework.api

import java.sql.Timestamp

data class RestResponse<T>(val success: Boolean, val payload: T, val timestamp: Timestamp = Timestamp(System.currentTimeMillis()))
