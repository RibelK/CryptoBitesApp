package com.example.crypto.data

import retrofit2.http.GET

interface BinanceApi {
    @GET("api/v3/ticker/24hr")
    suspend fun getTicker24hr(): List<CryptoTicker>
}
