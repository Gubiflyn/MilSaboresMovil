package com.example.proyectologin005d.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.proyectologin005d.data.model.User
import com.example.proyectologin005d.ui.login.LoginUiState
import com.example.proyectologin005d.ui.login.LoginViewModel
import android.util.Patterns
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (User) -> Unit,
    vm: LoginViewModel = viewModel()
) {
    val state: LoginUiState = vm.uiState
    var showPass by remember { mutableStateOf(false) }

    // ---- Validaciones visibles ----
    val emailValid by remember(state.username) {
        mutableStateOf(
            state.username.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(state.username).matches()
        )
    }
    val passValid by remember(state.password) { mutableStateOf(state.password.length >= 6) }

    val emailErrorMsg by remember(state.username) {
        mutableStateOf(
            when {
                state.username.isBlank() -> "El email es requerido"
                !Patterns.EMAIL_ADDRESS.matcher(state.username).matches() -> "Formato inválido"
                else -> null
            }
        )
    }
    val passErrorMsg by remember(state.password) {
        mutableStateOf(
            when {
                state.password.isBlank() -> "La contraseña es requerida"
                state.password.length < 6 -> "Mínimo 6 caracteres"
                else -> null
            }
        )
    }

    val scheme = darkColorScheme(
        primary = Color(0xFF8B4513),
        onPrimary = Color.White,
        onSurface = Color(0xFF333333)
    )

    MaterialTheme(colorScheme = scheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF5E1))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Mil Sabores",
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(140.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(20.dp))
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
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0x99000000)),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // ---------- EMAIL ----------
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
            OutlinedTextField(
                value = state.username,
                onValueChange = vm::onUsernameChange,
                placeholder = { Text("ejemplo@gmail.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = emailErrorMsg != null,
                supportingText = { if (emailErrorMsg != null) Text(emailErrorMsg!!) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8B4513),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    cursorColor = Color(0xFF8B4513),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(12.dp))

            // ---------- PASSWORD ----------
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
            OutlinedTextField(
                value = state.password,
                onValueChange = vm::onPasswordChange,
                placeholder = { Text("••••••••") },
                singleLine = true,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { showPass = !showPass }) {
                        Text(if (showPass) "Ocultar" else "Ver", color = Color(0xFF8B4513))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = passErrorMsg != null,
                supportingText = { if (passErrorMsg != null) Text(passErrorMsg!!) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8B4513),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    cursorColor = Color(0xFF8B4513),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // ---------- ERROR DE LOGIN ----------
            state.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    color = Color(0xFF8B4513),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(24.dp))

            // ---------- BOTÓN INICIAR SESIÓN ----------
            Button(
                onClick = {
                    if (emailValid && passValid && !state.isLoading) {
                        vm.submit { user ->
                            Log.d("LoginScreen", "LOGIN OK, navegando a animación")
                            onLoginSuccess(user)
                            navController.navigate("login_animation") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                enabled = !state.isLoading && emailValid && passValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp),
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

            Spacer(Modifier.height(12.dp))

            // ---------- INVITADO (AHORA CON ANIMACIÓN 👀) ----------
            OutlinedButton(
                onClick = {
                    navController.navigate("guest_animation") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF8B4513)),
                border = BorderStroke(1.dp, Color(0xFF8B4513))
            ) {
                Text("Entrar como invitado", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0x99000000))
                )
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF8B4513),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { navController.navigate("register") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val nav = rememberNavController()
    LoginScreen(navController = nav, onLoginSuccess = {})
}



