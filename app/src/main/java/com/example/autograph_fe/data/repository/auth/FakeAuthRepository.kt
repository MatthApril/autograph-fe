package com.example.autograph_fe.data.repository.auth

import com.example.autograph_fe.models.OtpTiming
import com.example.autograph_fe.models.User
import kotlinx.coroutines.delay

/**
 * Pretends to be the server, for previews and for working without it.
 * Test inputs: password "wrongpass" fails login; emails ending @taken.com / @pending.com fail register;
 * code 123456 works, 000000 = no internet, anything else = wrong code.
 */
class FakeAuthRepository : AuthRepository {

    override suspend fun logIn(email: String, password: String): Result<User> {
        delay(1_000)
        return if (password == "wrongpass") Result.failure(AuthError.InvalidCredentials)
        else Result.success(User(id = 1, name = "Maya", email = email, pictureUrl = null))
    }

    override suspend fun register(name: String, email: String, password: String): Result<OtpTiming> {
        delay(1_000)
        return when {
            email.endsWith("@taken.com") -> Result.failure(AuthError.EmailTaken)
            email.endsWith("@pending.com") -> Result.failure(AuthError.VerificationPending)
            else -> Result.success(OtpTiming(email, expiresInSeconds = 60, resendInSeconds = 60))
        }
    }

    override suspend fun verifyEmail(email: String, code: String): Result<User> {
        delay(800)
        return when (code) {
            "123456" -> Result.success(
                User(
                    id = 1,
                    name = "Maya",
                    email = email,
                    pictureUrl = null
                )
            )
            "000000" -> Result.failure(AuthError.Network)
            else -> Result.failure(AuthError.InvalidCode(attemptsLeft = 4))
        }
    }

    override suspend fun resendCode(email: String): Result<OtpTiming> {
        delay(800)
        return Result.success(OtpTiming(email, expiresInSeconds = 60, resendInSeconds = 60))
    }
}