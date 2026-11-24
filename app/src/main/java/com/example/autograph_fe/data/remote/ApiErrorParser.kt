package com.example.autograph_fe.data.remote

import com.example.autograph_fe.data.remote.dto.ApiErrorBody
import com.example.autograph_fe.data.repository.auth.AuthError
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException

/** Turns a failed request (HTTP error or no connection) into an [AuthError]. */
class ApiErrorParser(moshi: Moshi) {

    private val adapter = moshi.adapter(ApiErrorBody::class.java)

    fun parse(error: Throwable): AuthError = when (error) {
        is AuthError -> error
        is IOException -> AuthError.Network            // no internet, server down, timeout
        is HttpException -> fromBody(error.response()?.errorBody()?.string())
        else -> AuthError.Unknown(error.message)
    }

    private fun fromBody(json: String?): AuthError {
        val body = json?.let { runCatching { adapter.fromJson(it) }.getOrNull() }
        return when (body?.code) {
            "OTP_INVALID" -> AuthError.InvalidCode(attemptsLeft = body.intData("attempts_left"))
            "OTP_EXPIRED" -> AuthError.CodeExpired
            "NO_PENDING_SIGNUP" -> AuthError.SignUpTimedOut
            "TOO_MANY_ATTEMPTS" -> AuthError.TooManyAttempts
            "RESEND_TOO_SOON" -> AuthError.ResendTooSoon(body.intData("resend_available_in") ?: 60)
            "VERIFICATION_PENDING" -> AuthError.VerificationPending
            "EMAIL_TAKEN" -> AuthError.EmailTaken
            "INVALID_CREDENTIALS" -> AuthError.InvalidCredentials
            else -> AuthError.Unknown(body?.message)
        }
    }

    // Moshi reads every number inside a Map as a Double, so convert carefully
    private fun ApiErrorBody.intData(key: String): Int? = (data?.get(key) as? Number)?.toInt()
}
