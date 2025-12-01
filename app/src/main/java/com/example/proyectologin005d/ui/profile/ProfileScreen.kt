package com.example.proyectologin005d.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectologin005d.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel
) {
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)

    val context = LocalContext.current

    val user by authViewModel.user.collectAsState(initial = null)

    LaunchedEffect(user) {
        profileViewModel.setFromUser(user)
    }

    val ui by profileViewModel.uiState.collectAsState()

    // ✅ Estado local para la foto de perfil
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Función para convertir un Uri de la galería en Bitmap
    fun loadBitmapFromUri(uri: Uri): Bitmap? =
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input)
            }
        } catch (e: Exception) {
            null
        }

    // Lanzador para la cámara (foto previa, suficiente para un avatar)
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                profileBitmap = bitmap
            }
        }

    // Lanzador para la galería
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                loadBitmapFromUri(it)?.let { bmp ->
                    profileBitmap = bmp
                }
            }
        }

    // Lanzador para pedir permiso de cámara
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                cameraLauncher.launch(null)
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }

    Scaffold(
        containerColor = Cream
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

            // ✅ Avatar + botones de cámara / galería
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE6C7)),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileBitmap != null) {
                        Image(
                            bitmap = profileBitmap!!.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Foto de perfil por defecto",
                            tint = Brown,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    OutlinedButton(
                        onClick = {
                            // Pide permiso y luego abre la cámara
                            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    ) {
                        Text("Tomar foto")
                    }

                    Button(
                        onClick = {
                            // Abre el selector del sistema para elegir imagen
                            galleryLauncher.launch("image/*")
                        }
                    ) {
                        Text("Elegir de galería")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ✅ Tarjeta con la info del usuario
            if (ui.isGuest) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
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

                        ui.edad?.let { edad ->
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Edad: $edad",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

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

            // Botón para ir a la pantalla de ubicación (API externa)
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("location_api") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Ver mi ubicación (API externa)")
            }
        }
    }
}
