package com.example.proyectologin005d.ui.login

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
fun LoginAnimationScreen(navController: NavController) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("login_success.json")
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("home") {
            popUpTo("login") { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5E1)),
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
                text = "Bienvenido a Mil Sabores!!",
                color = Color(0xFF8B4513),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}



