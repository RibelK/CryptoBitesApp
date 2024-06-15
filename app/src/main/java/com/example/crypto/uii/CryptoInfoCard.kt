package com.example.crypto.uii

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.crypto.data.CryptoInfo

@Composable
fun CryptoInfoCard(cryptoInfo: CryptoInfo, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("crypto_price/${cryptoInfo.symbol}/${cryptoInfo.price}")
            },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = cryptoInfo.iconUrl),
                contentDescription = "Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                val symbolWithoutUsdt = cryptoInfo.symbol.removeSuffix("USDT")
                Text(text = "Symbol: $symbolWithoutUsdt")

                val price = cryptoInfo.price.toDoubleOrNull() ?: 0.0
                val previousPrice = cryptoInfo.previousPrice.toDoubleOrNull() ?: 0.0
                val priceChangeIcon = when {
                    price > previousPrice -> Icons.Default.ArrowUpward
                    price < previousPrice -> Icons.Default.ArrowDownward
                    else -> null
                }
                val priceColor = when {
                    price > previousPrice -> Color.Green
                    price < previousPrice -> Color.Red
                    else -> Color.Unspecified
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Price: ${cryptoInfo.price}",
                        color = priceColor
                    )
                    if (priceChangeIcon != null) {
                        Icon(
                            imageVector = priceChangeIcon,
                            contentDescription = "Price Change Icon",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(text = "Volume: ${cryptoInfo.volume}")
                Text(text = "Market Cap: ${cryptoInfo.marketCap}")
            }
        }
    }
}
