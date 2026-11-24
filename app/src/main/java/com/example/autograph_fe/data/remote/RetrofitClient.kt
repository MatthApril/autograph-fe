package com.example.autograph_fe.data.remote

import android.content.Context
import com.example.autograph_fe.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private lateinit var retrofitInstance: Retrofit

    fun initialize(context: Context) {
        if (::retrofitInstance.isInitialized) return

        retrofitInstance = Retrofit.Builder()
            .baseUrl(context.getString(R.string.api_base_url))
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            )
            .build()
    }

    val retrofit: Retrofit
        get() = retrofitInstance
}