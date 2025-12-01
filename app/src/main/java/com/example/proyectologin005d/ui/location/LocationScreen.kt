package com.example.proyectologin005d.ui.location

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Cargar la ubicación al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadLocation()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi ubicación (API externa)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${state.errorMessage}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadLocation() }) {
                            Text("Reintentar")
                        }
                    }
                }

                state.location != null -> {
                    // ✅ Aquí aseguramos que no es null
                    val loc = state.location!!

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Datos obtenidos desde ipapi.co",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "IP: ${loc.ip ?: "-"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Ciudad: ${loc.city ?: "-"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Región: ${loc.region ?: "-"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "País: ${loc.country_name ?: "-"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Latitud: ${loc.latitude ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Longitud: ${loc.longitude ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
