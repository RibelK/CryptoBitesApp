package com.example.crypto.data

data class CryptoInfo(
    val symbol: String,
    val price: String,
    val volume: String,
    val marketCap: String,
    val iconUrl: String,
    val previousPrice: String = price
)
