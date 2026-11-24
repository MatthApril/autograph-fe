package com.example.autograph_fe

import com.example.autograph_fe.data.remote.ApiErrorParser
import com.example.autograph_fe.data.repository.AuthError
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

/** Checks that each server error becomes the right AuthError, without a server. */
class ApiErrorParserTest {

    private val parser = ApiErrorParser(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())

    /** Builds the exception Retrofit throws for an error response. */
    private fun httpError(status: Int, body: String) = HttpException(
        Response.error<Any>(status, body.toResponseBody("application/json".toMediaType()))
    )

    @Test
    fun wrongCode_readsAttemptsLeft() {
        val json = """{"status":400,"code":"OTP_INVALID","message":"x","data":{"attempts_left":3}}"""
        assertEquals(AuthError.InvalidCode(attemptsLeft = 3), parser.parse(httpError(400, json)))
    }

    @Test
    fun resendTooSoon_readsWaitSeconds() {
        val json = """{"status":429,"code":"RESEND_TOO_SOON","message":"x","data":{"resend_available_in":42}}"""
        assertEquals(AuthError.ResendTooSoon(waitSeconds = 42), parser.parse(httpError(429, json)))
    }

    @Test
    fun noConnection_isNetworkError() {
        assertEquals(AuthError.Network, parser.parse(UnknownHostException()))
    }

    @Test
    fun htmlErrorPage_isUnknownNotACrash() {
        assertTrue(parser.parse(httpError(502, "<html>Bad gateway</html>")) is AuthError.Unknown)
    }
}
