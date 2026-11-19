package com.example.autograph_fe.data.remote

import com.example.autograph_fe.data.remote.services.AuthService

object ApiService {
    val auth: AuthService by lazy {
        RetrofitClient.retrofit.create(AuthService::class.java)
    }

    

}