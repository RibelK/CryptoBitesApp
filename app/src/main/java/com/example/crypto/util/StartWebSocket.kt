package com.example.crypto.util

import com.example.crypto.data.CryptoInfo
import com.example.crypto.data.iconUrlMap
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

@OptIn(DelicateCoroutinesApi::class)
fun startWebSocket(onUpdate: (CryptoInfo) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("wss://stream.binance.com:9443/ws/!ticker@arr")
        .build()

    val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // WebSocket opened
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(List::class.java)
            val cryptoTickerList = jsonAdapter.fromJson(text) as List<Map<String, Any>>

            cryptoTickerList.forEach { ticker ->
                val symbol = ticker["s"] as String
                if (symbol.endsWith("USDT")) {
                    val updatedInfo = CryptoInfo(
                        symbol = symbol,
                        price = ticker["c"] as String,
                        volume = ticker["v"] as String,
                        marketCap = "",
                        iconUrl = iconUrlMap[symbol] ?: ""
                    )
                    GlobalScope.launch(Dispatchers.Main) {
                        onUpdate(updatedInfo)
                    }
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            onMessage(webSocket, bytes.utf8())
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            t.printStackTrace()
        }
    }

    client.newWebSocket(request, webSocketListener)
    client.dispatcher.executorService.shutdown()
}
