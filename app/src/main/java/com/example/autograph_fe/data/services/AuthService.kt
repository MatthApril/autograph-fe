package com.example.autograph_fe.data.remote.services

import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("auth/google")
    suspend fun googleLogin()

    @POST("auth/login")
    suspend fun login()

    @POST("auth/register")
    suspend fun register()

    @POST("auth/logout")
    suspend fun logout()

    @GET("/auth/refresh")
    suspend fun refresh()

}