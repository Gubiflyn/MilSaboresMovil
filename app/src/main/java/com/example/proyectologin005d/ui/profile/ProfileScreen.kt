package com.example.proyectologin005d.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectologin005d.ui.auth.AuthViewModel
import com.example.proyectologin005d.ui.home.HomeBottomBar   // 👈 Reutilizamos la barra inferior del Home

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(),
    authViewModel: com.example.proyectologin005d.ui.auth.AuthViewModel
) {
    // Colores iguales al HomeScreen
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)

    // Lee el usuario actual desde tu AuthViewModel (StateFlow<User?>)
    val user by authViewModel.user.collectAsState(initial = null)

    // Sincroniza la VM de perfil cuando cambie el usuario
    LaunchedEffect(user) {
        profileViewModel.setFromUser(user)
    }

    // Estado de UI ya mapeado a tu modelo real
    val ui by profileViewModel.uiState.collectAsState()

    // ---------- UI con mismo fondo y barra inferior ----------
    Scaffold(
        containerColor = Cream,
        bottomBar = {
            HomeBottomBar(
                current = "profile",
                onHome = { navController.navigate("home") },
                onSearch = { navController.navigate("catalog") },
                onHistory = { /* navController.navigate("history") */ },
                onProfile = { /* ya estás en perfil */ },
                brown = Brown
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Perfil",
                tint = Brown
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(16.dp))

            if (ui.isGuest) {
                // Sesión de invitado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Sesión de invitado",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Inicia sesión para ver tus datos y beneficios.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                // Datos del usuario autenticado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = ui.nombre.ifBlank { "Nombre no disponible" },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = ui.email.ifBlank { "Email no disponible" },
                            style = MaterialTheme.typography.bodyMedium
                        )

                        // Edad (si viene)
                        ui.edad?.let { edad ->
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Edad: $edad",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Beneficios / Descuentos detectados
                        val beneficios = buildList {
                            if (ui.tiene50) add("50% por edad (≥ 50)")
                            if (ui.tiene10) add("10% por código FELICES50")
                            if (!ui.codigoDescuento.isNullOrBlank()) add("Código: ${ui.codigoDescuento}")
                        }

                        if (beneficios.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Beneficios:",
                                style = MaterialTheme.typography.titleSmall
                            )
                            beneficios.forEach { b ->
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "• $b",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
