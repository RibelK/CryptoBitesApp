package com.example.crypto.uii

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.crypto.util.startWebSocket
import com.github.mikephil.charting.data.Entry
import com.example.crypto.data.iconUrlMap

@Composable
fun CryptoPriceScreen(symbol: String, price: String) {
    val context = LocalContext.current
    var historicalData by remember { mutableStateOf(listOf<Entry>()) }
    var currentPrice by remember { mutableStateOf(price) }

    val symbolWithoutUsdt = symbol.removeSuffix("USDT")
    val iconUrl = iconUrlMap[symbol]

    LaunchedEffect(Unit) {
        startWebSocket { updatedInfo ->
            if (updatedInfo.symbol == symbol) {
                currentPrice = updatedInfo.price
                val newEntry = Entry(historicalData.size.toFloat(), updatedInfo.price.toFloat())
                historicalData = historicalData + newEntry
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconUrl != null) {
                Image(
                    painter = rememberImagePainter(data = iconUrl),
                    contentDescription = "$symbolWithoutUsdt icon",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = "$symbolWithoutUsdt Details",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Current Price: $currentPrice",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    color = Color.Green
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Price History", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        LineChartView(context = context, data = historicalData)
    }
}