package com.example.proyectologin005d.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectologin005d.R
import com.example.proyectologin005d.ui.register.RegisterViewModel
import android.util.Patterns // ← CORRECTO

@Composable
fun RegisterScreen(
    navController: NavController,
    vm: RegisterViewModel = viewModel()
) {
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)

    val state = vm.uiState

    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNac by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var codDesc by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    // ---- Validaciones visibles ----
    val emailValid by remember(state.email) {
        mutableStateOf(
            state.email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
        )
    }
    val passValid by remember(state.password) { mutableStateOf(state.password.length >= 6) }
    val passMatch by remember(state.password, state.confirmPassword) {
        mutableStateOf(state.confirmPassword == state.password)
    }

    val emailErrorMsg by remember(state.email) {
        mutableStateOf(
            when {
                state.email.isBlank() -> "Email requerido"
                !Patterns.EMAIL_ADDRESS.matcher(state.email).matches() -> "Email inválido"
                else -> null
            }
        )
    }
    val passErrorMsg by remember(state.password) {
        mutableStateOf(
            when {
                state.password.isBlank() -> "Contraseña requerida"
                state.password.length < 6 -> "Mínimo 6 caracteres"
                else -> null
            }
        )
    }
    val pass2ErrorMsg by remember(state.confirmPassword, state.password) {
        mutableStateOf(
            when {
                state.confirmPassword.isBlank() -> "Confirme su contraseña"
                state.confirmPassword != state.password -> "No coincide"
                else -> null
            }
        )
    }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Brown,
        unfocusedBorderColor = Color(0xFFBDBDBD),
        cursorColor = Brown,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Brown,
            onPrimary = Color.White,
            onSurface = Color(0xFF333333)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                // Logo + título
                item {
                    Spacer(Modifier.height(48.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo Mil Sabores",
                            modifier = Modifier
                                .fillMaxWidth(0.55f)
                                .height(130.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Registro de Usuario",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF000000)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Nombres (VM)
                item {
                    LabeledField(
                        label = "Nombres *",
                        value = state.nombre,
                        onChange = vm::onNombreChange,
                        colors = inputColors
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Apellidos
                item {
                    LabeledField(
                        label = "Apellidos *",
                        value = apellidos,
                        onChange = { apellidos = it },
                        colors = inputColors
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Email (VM)
                item {
                    LabeledField(
                        label = "Correo *",
                        value = state.email,
                        onChange = vm::onEmailChange,
                        keyboardType = KeyboardType.Email,
                        placeholder = "ejemplo@gmail.com",
                        colors = inputColors
                    )
                    if (emailErrorMsg != null) Text(emailErrorMsg!!, color = Brown, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                }

                // Password (VM)
                item {
                    LabeledPassword(
                        label = "Contraseña *",
                        value = state.password,
                        onChange = vm::onPasswordChange,
                        colors = inputColors
                    )
                    if (passErrorMsg != null) Text(passErrorMsg!!, color = Brown, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                }

                // Confirmar Password (VM)
                item {
                    LabeledPassword(
                        label = "Confirmar contraseña *",
                        value = state.confirmPassword,
                        onChange = vm::onConfirmPasswordChange,
                        colors = inputColors
                    )
                    if (pass2ErrorMsg != null) Text(pass2ErrorMsg!!, color = Brown, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                }

                // Teléfono
                item {
                    LabeledField(
                        label = "Teléfono",
                        value = telefono,
                        onChange = { telefono = it },
                        keyboardType = KeyboardType.Phone,
                        colors = inputColors
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Fecha de nacimiento
                item {
                    LabeledField(
                        label = "Fecha de nacimiento *",
                        value = fechaNac,
                        onChange = { fechaNac = it },
                        keyboardType = KeyboardType.Number,
                        placeholder = "dd/mm/aaaa",
                        colors = inputColors
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Dirección
                item {
                    LabeledField(
                        label = "Dirección *",
                        value = direccion,
                        onChange = { direccion = it },
                        colors = inputColors
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Región
                item {
                    LabeledField(
                        label = "Región *",
                        value = region,
                        onChange = { region = it },
                        colors = inputColors
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Comuna
                item {
                    LabeledField(
                        label = "Comuna *",
                        value = comuna,
                        onChange = { comuna = it },
                        colors = inputColors
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Código de descuento (opcional)
                item {
                    LabeledField(
                        label = "Código de descuento (opcional)",
                        value = codDesc,
                        onChange = { codDesc = it },
                        placeholder = "FELICES50",
                        colors = inputColors
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // Mensaje de error (local o del VM)
                item {
                    val error = localError ?: state.error
                    if (error != null) {
                        Text(
                            text = error,
                            color = Brown,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                        )
                    }
                }

                // Botón Registrarse
                item {
                    Button(
                        onClick = {
                            localError = when {
                                !emailValid -> emailErrorMsg
                                !passValid -> passErrorMsg
                                !passMatch -> pass2ErrorMsg
                                apellidos.isBlank() -> "Ingresa tus apellidos"
                                fechaNac.isBlank() -> "Ingresa tu fecha de nacimiento"
                                direccion.isBlank() -> "Ingresa tu dirección"
                                region.isBlank() -> "Ingresa tu región"
                                comuna.isBlank() -> "Ingresa tu comuna"
                                else -> null
                            }
                            if (localError == null) {
                                vm.submit {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        },
                        enabled = !state.isLoading && emailValid && passValid && passMatch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Brown,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (state.isLoading) "Creando cuenta..." else "Registrarse",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // Botón secundario: Iniciar sesión
                item {
                    OutlinedButton(
                        onClick = {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Brown
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                    ) {
                        Text("Iniciar sesión")
                    }
                    Spacer(Modifier.height(20.dp))
                }

                // Enlace adicional
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "¿Ya tienes cuenta? ", color = Color(0x99000000))
                        Text(
                            text = "Inicia sesión",
                            color = Brown,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/* ---------- Helpers UI ---------- */

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholder: String? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    val Brown = Color(0xFF8B4513)
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold, color = Brown
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            placeholder = { if (placeholder != null) Text(placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            colors = colors
        )
    }
}

@Composable
private fun LabeledPassword(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    val Brown = Color(0xFF8B4513)
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold, color = Brown
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = colors
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    val nav = rememberNavController()
    RegisterScreen(navController = nav)
}
