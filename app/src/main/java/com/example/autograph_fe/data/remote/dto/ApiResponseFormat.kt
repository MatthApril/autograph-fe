package com.example.autograph_fe.data.remote.dto

/** Every success response: { status, message, data } */
data class ApiResponse<T>(
    val status: Int,
    val message: String?,
    val data: T?,
)

/** Every error response: { status, code, message, data } */
data class ApiErrorBody(
    val status: Int?,
    val code: String?,
    val message: String?,
    val data: Map<String, Any?>?,
)