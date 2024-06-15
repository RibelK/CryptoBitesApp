package com.example.crypto.uii

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crypto.data.CryptoInfo
import com.example.crypto.util.fetchCryptoInfo
import com.example.crypto.util.startWebSocket

@Composable
fun CryptoInfoScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("crypto_list") { CryptoListScreen(navController) }
        composable("crypto_price/{symbol}/{price}") { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: ""
            CryptoPriceScreen(symbol = symbol, price = price)
        }
    }
}

@Composable
fun CryptoListScreen(navController: NavHostController) {
    var cryptoInfoList by remember { mutableStateOf(listOf<CryptoInfo>()) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchCryptoInfo { updatedList ->
            cryptoInfoList = updatedList
        }
    }

    LaunchedEffect(Unit) {
        startWebSocket { updatedInfo ->
            cryptoInfoList = cryptoInfoList.map { info ->
                if (info.symbol == updatedInfo.symbol) {
                    info.copy(
                        price = updatedInfo.price,
                        volume = updatedInfo.volume,
                        previousPrice = info.price
                    )
                } else {
                    info
                }
            }.sortedByDescending { it.price.toDouble() }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filteredList = cryptoInfoList.filter {
                it.symbol.contains(searchQuery, ignoreCase = true)
            }
            items(filteredList) { cryptoInfo ->
                CryptoInfoCard(cryptoInfo, navController)
            }
        }
    }
}