package com.example.autograph_fe.data.repository

import com.example.autograph_fe.auth.TokenManager
import com.example.autograph_fe.data.remote.ApiErrorParser
import com.example.autograph_fe.data.remote.dto.ApiResponse
import com.example.autograph_fe.data.remote.dto.EmailRequest
import com.example.autograph_fe.data.remote.dto.LoginRequest
import com.example.autograph_fe.data.remote.dto.OtpTimingDto
import com.example.autograph_fe.data.remote.dto.RegisterRequest
import com.example.autograph_fe.data.remote.dto.UserDto
import com.example.autograph_fe.data.remote.dto.VerifyEmailRequest
import com.example.autograph_fe.data.remote.services.AuthService
import com.example.autograph_fe.models.OtpTiming
import com.example.autograph_fe.models.User
import kotlin.coroutines.cancellation.CancellationException

class RemoteAuthRepository(
    private val service: AuthService,
    private val tokenManager: TokenManager,
    private val errorParser: ApiErrorParser,
) : AuthRepository {

    override suspend fun logIn(email: String, password: String): Result<User> = safeCall {
        val session = service.login(LoginRequest(email, password)).requireData()
        tokenManager.saveTokens(session.accessToken, session.refreshToken)
        session.user.toUser()
    }

    override suspend fun register(name: String, email: String, password: String): Result<OtpTiming> =
        safeCall {
            // The app already checked that both passwords match, so it sends the same one twice
            service.register(RegisterRequest(name, email, password, confirmPassword = password))
                .requireData()
                .toOtpTiming()
        }

    override suspend fun verifyEmail(email: String, code: String): Result<User> = safeCall {
        val session = service.verifyEmail(VerifyEmailRequest(email, code)).requireData()
        tokenManager.saveTokens(session.accessToken, session.refreshToken)
        session.user.toUser()
    }

    override suspend fun resendCode(email: String): Result<OtpTiming> = safeCall {
        service.resendOtp(EmailRequest(email)).requireData().toOtpTiming()
    }

    /** Runs a request and turns any failure into Result.failure(AuthError). */
    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e      // never swallow cancellation: the screen was closed, let the coroutine stop
        } catch (e: Exception) {
            Result.failure(errorParser.parse(e))
        }
}

// Mappers: DTO in, app model out
private fun <T> ApiResponse<T>.requireData(): T = data ?: throw AuthError.Unknown(message)

private fun UserDto.toUser() = User(id = userId, name = name.orEmpty(), email = email, pictureUrl = picture)

private fun OtpTimingDto.toOtpTiming() = OtpTiming(email, otpExpiresIn, resendAvailableIn)
