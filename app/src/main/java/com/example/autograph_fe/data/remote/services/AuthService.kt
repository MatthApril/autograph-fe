package com.example.autograph_fe.data.remote.services

import com.example.autograph_fe.data.remote.dto.ApiResponse
import com.example.autograph_fe.data.remote.dto.EmailRequest
import com.example.autograph_fe.data.remote.dto.LoginRequest
import com.example.autograph_fe.data.remote.dto.OtpTimingDto
import com.example.autograph_fe.data.remote.dto.RegisterRequest
import com.example.autograph_fe.data.remote.dto.SessionDto
import com.example.autograph_fe.data.remote.dto.VerifyEmailRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// Paths match authRoutes.js exactly, with no leading slash, so they're added to the /api/ base URL
interface AuthService {

    @POST("register")
    suspend fun register(@Body body: RegisterRequest): ApiResponse<OtpTimingDto>

    @POST("verify-email")
    suspend fun verifyEmail(@Body body: VerifyEmailRequest): ApiResponse<SessionDto>

    @POST("refresh-otp")
    suspend fun resendOtp(@Body body: EmailRequest): ApiResponse<OtpTimingDto>

    @POST("login")
    suspend fun login(@Body body: LoginRequest): ApiResponse<SessionDto>

    // Unchanged; it gets a proper return type when Google sign-in is finished
    @FormUrlEncoded
    @POST("auth/google")
    suspend fun signInWithGoogleService(@Field("id_token") id_token: String)
}
