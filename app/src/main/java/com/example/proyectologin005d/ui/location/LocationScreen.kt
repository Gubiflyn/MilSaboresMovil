package com.example.proyectologin005d.ui.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Cargar la ubicación por API externa al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadLocation()
    }

    // --------- Estados y helpers para ubicación por GPS ---------
    val context = LocalContext.current
    val fusedClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var gpsLat by remember { mutableStateOf<Double?>(null) }
    var gpsLon by remember { mutableStateOf<Double?>(null) }
    var gpsError by remember { mutableStateOf<String?>(null) }
    var gpsIsLoading by remember { mutableStateOf(false) }

    fun getGpsLocation() {
        gpsIsLoading = true
        gpsError = null

        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                gpsIsLoading = false
                if (location != null) {
                    gpsLat = location.latitude
                    gpsLon = location.longitude
                } else {
                    gpsError = "No se pudo obtener la ubicación del dispositivo."
                }
            }
            .addOnFailureListener { e ->
                gpsIsLoading = false
                gpsError = e.message ?: "Error al obtener la ubicación por GPS."
            }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                getGpsLocation()
            } else {
                gpsIsLoading = false
                gpsError = "Permiso de ubicación denegado."
            }
        }
    // ------------------------------------------------------------

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
                    val loc = state.location!!

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // ------- Sección 1: API externa (ipwho.is) -------
                        Text(
                            text = "Datos obtenidos desde ipwho.is",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text = "Ubicación aproximada basada en la IP pública.",
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
                            text = "Latitud (IP): ${loc.latitude ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Longitud (IP): ${loc.longitude ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(Modifier.height(24.dp))
                        Divider()
                        Spacer(Modifier.height(16.dp))

                        // ------- Sección 2: Ubicación exacta por GPS -------
                        Text(
                            text = "Ubicación exacta (GPS del dispositivo)",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = {
                                gpsError = null
                                // Verificar permiso antes de pedir ubicación
                                val granted = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED

                                if (granted) {
                                    getGpsLocation()
                                } else {
                                    gpsIsLoading = true
                                    locationPermissionLauncher.launch(
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                }
                            }
                        ) {
                            Text("Obtener ubicación por GPS")
                        }

                        Spacer(Modifier.height(8.dp))

                        when {
                            gpsIsLoading -> {
                                Text(
                                    text = "Obteniendo ubicación por GPS...",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            gpsError != null -> {
                                Text(
                                    text = gpsError ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            gpsLat != null && gpsLon != null -> {
                                Text(
                                    text = "Latitud (GPS): $gpsLat",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Longitud (GPS): $gpsLon",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
