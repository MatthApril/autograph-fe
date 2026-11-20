package com.example.autograph_fe.data.remote.services

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("auth/google")
    suspend fun signInWithGoogleService(
        @Field("id_token") id_token: String
    )

    @POST("auth/login")
    suspend fun login()

    @POST("auth/register")
    suspend fun register()

    @POST("auth/logout")
    suspend fun logout()

    @GET("/auth/refresh")
    suspend fun refresh()

}