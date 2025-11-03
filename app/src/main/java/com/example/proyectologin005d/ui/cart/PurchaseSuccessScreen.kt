package com.example.proyectologin005d.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun PurchaseSuccessScreen(navController: NavController) {
    // Animación desde assets/purchase_success.json
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("purchase_success.json")
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    // Espera 2.5s y va al historial
    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("history") {
            popUpTo("cart") { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5E1)), // fondo crema
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Compra exitosa! 🎉",
                color = Color(0xFF8B4513), // café
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
