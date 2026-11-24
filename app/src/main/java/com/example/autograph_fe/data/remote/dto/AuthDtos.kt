package com.example.autograph_fe.data.remote.dto

import com.squareup.moshi.Json

// Requests
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @Json(name = "confirm_password") val confirmPassword: String,
)
data class VerifyEmailRequest(val email: String, val otp: String)
data class EmailRequest(val email: String)
data class LoginRequest(val email: String, val password: String)

// Responses
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

data class OtpTimingDto(
    val email: String,
    @Json(name = "otp_expires_in") val otpExpiresIn: Int,
    @Json(name = "resend_available_in") val resendAvailableIn: Int,
)

data class UserDto(
    @Json(name = "user_id") val userId: Int,
    val name: String?,
    val email: String,
    val picture: String?,
)

data class SessionDto(
    val user: UserDto,
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
)
