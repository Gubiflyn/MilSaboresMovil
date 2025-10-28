package com.example.proyectologin005d.login

// ====== IMPORTS ======
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectologin005d.R
import com.example.proyectologin005d.ui.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {
    // ✅ Obtener el ViewModel aquí (NO como valor por defecto de un parámetro)
    val vm: LoginViewModel = viewModel()
    val state = vm.uiState
    var showPass by remember { mutableStateOf(false) }

    // 🎨 Paleta principal
    val ColorScheme = darkColorScheme(
        primary = Color(0xFF8B4513), // Marrón
        onPrimary = Color.White,
        onSurface = Color(0xFF333333)
    )

    MaterialTheme(colorScheme = ColorScheme) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF5E1)) // Fondo crema
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ======= LOGO =======
            Spacer(modifier = Modifier.height(60.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Mil Sabores",
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(140.dp),
                contentScale = ContentScale.Fit
            )

            // ======= TÍTULOS =======
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Inicia Sesión",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                )
            )
            Text(
                text = "¡Hola! un gusto volver a verte",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0x99000000)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ======= FORMULARIO =======
            Text(
                text = "Email",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            // Campo de Email (blanco)
            OutlinedTextField(
                value = state.username,
                onValueChange = vm::onUsernameChange,
                placeholder = { Text("ejemplo@gmail.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8B4513),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    cursorColor = Color(0xFF8B4513),
                    focusedContainerColor = Color.White,   // ✅ Fondo blanco
                    unfocusedContainerColor = Color.White  // ✅ Fondo blanco
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Password",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            // Campo de contraseña (blanco)
            OutlinedTextField(
                value = state.password,
                onValueChange = vm::onpasswordChange,
                placeholder = { Text("••••••••") },
                singleLine = true,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { showPass = !showPass }) {
                        Text(
                            if (showPass) "Ocultar" else "Ver",
                            color = Color(0xFF8B4513)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8B4513),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    cursorColor = Color(0xFF8B4513),
                    focusedContainerColor = Color.White,   // ✅ Fondo blanco
                    unfocusedContainerColor = Color.White  // ✅ Fondo blanco
                )
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error ?: "",
                    color = Color(0xFF8B4513),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ======= BOTÓN INICIAR SESIÓN =======
            Button(
                onClick = {
                    vm.submit { user ->
                        navController.navigate("DrawerMenu/$user") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B4513),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (state.isLoading) "Validando..." else "Iniciar Sesión",
                    fontWeight = FontWeight.Bold
                )
            }

            // ======= OLVIDASTE / REGISTRO =======
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0x99000000)
                    )
                )
                // 👇 Hace clic y navega a la ruta "register"
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF8B4513),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }
        }
    }
}


