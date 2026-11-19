
package com.example.autograph_fe.data.remote

import android.app.Application
import com.example.autograph_fe.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient : Application() {

    companion object {
        private lateinit var instance: RetrofitClient

        private val moshi by lazy {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(instance.getString(R.string.api_base_url))
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
