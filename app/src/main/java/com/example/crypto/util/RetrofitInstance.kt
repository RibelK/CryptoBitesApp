package com.example.crypto.util

import com.example.crypto.data.CriptoBitesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://hello-world-app.redbush-14610476.eastus.azurecontainerapps.io") // Altere para o endereço real da sua API
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val api: CriptoBitesApi by lazy {
        retrofit.create(CriptoBitesApi::class.java)
    }
}
