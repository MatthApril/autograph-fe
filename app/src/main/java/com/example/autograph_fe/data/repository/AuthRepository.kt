package com.example.autograph_fe.data.repository

import kotlinx.coroutines.delay

interface AuthRepository {
    suspend fun logIn(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
}

/** Pretends to be a server. Replace with a Retrofit version when the backend is ready. */
class FakeAuthRepository : AuthRepository {

    override suspend fun logIn(email: String, password: String): Result<Unit> {
        delay(1_000) // pretend the network takes one second
        return if (password == "wrongpass") {
            Result.failure(Exception("Incorrect email or password."))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        delay(1_000)
        return if (email.endsWith("@taken.com")) {
            Result.failure(Exception("An account with this email already exists."))
        } else {
            Result.success(Unit)
        }
    }
}
