package com.example.crypto.util

import com.example.crypto.data.BinanceApi
import com.example.crypto.data.CryptoInfo
import com.example.crypto.data.iconUrlMap
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@OptIn(DelicateCoroutinesApi::class)
fun fetchCryptoInfo(onUpdate: (List<CryptoInfo>) -> Unit) {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.binance.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service = retrofit.create(BinanceApi::class.java)

    GlobalScope.launch(Dispatchers.IO) {
        try {
            val tickers = service.getTicker24hr()
            val filteredTickers = tickers.filter { it.symbol.endsWith("USDT") }
                .sortedByDescending { it.lastPrice.toDouble() }
                .take(100)
            val cryptoInfoList = filteredTickers.map {
                CryptoInfo(
                    symbol = it.symbol,
                    price = it.lastPrice,
                    volume = it.volume,
                    marketCap = it.quoteVolume,
                    iconUrl = iconUrlMap[it.symbol] ?: ""
                )
            }
            withContext(Dispatchers.Main) {
                onUpdate(cryptoInfoList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
