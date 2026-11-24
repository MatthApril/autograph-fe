package com.example.autograph_fe.data.repository

import com.example.autograph_fe.models.OtpTiming
import com.example.autograph_fe.models.User

interface AuthRepository {
    suspend fun logIn(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<OtpTiming>
    suspend fun verifyEmail(email: String, code: String): Result<User>
    suspend fun resendCode(email: String): Result<OtpTiming>
}
