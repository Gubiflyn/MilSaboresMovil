package com.example.proyectologin005d.register

// ====== IMPORTS ======
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun RegisterScreen(
    navController: NavController,
    vm: RegisterViewModel = viewModel()
) {
    // ====== Paleta consistente con Login ======
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)

    // ====== Estado del ViewModel (nombre/email/password/confirm) ======
    val state = vm.uiState

    // ====== Estado local para campos adicionales ======
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNac by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var codDesc by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    // Colores para inputs (blancos con borde marrón)
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
            // ===== Contenido scrolleable =====
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                // Logo
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
                            // Validaciones adicionales a las que hace el VM
                            localError = when {
                                apellidos.isBlank() -> "Ingresa tus apellidos"
                                fechaNac.isBlank() -> "Ingresa tu fecha de nacimiento"
                                direccion.isBlank() -> "Ingresa tu dirección"
                                region.isBlank() -> "Ingresa tu región"
                                comuna.isBlank() -> "Ingresa tu comuna"
                                else -> null
                            }
                            if (localError == null) {
                                vm.submit {
                                    // Volver al login al terminar
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        },
                        enabled = !state.isLoading,
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

                // Botón secundario: Iniciar sesión (navega al LoginScreen)
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

                // Enlace de texto adicional (opcional)
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

/* ---------- Helpers de UI (etiqueta + OutlinedTextField) ---------- */

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

/* ---------------- PREVIEW ---------------- */
@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    val nav = rememberNavController()
    RegisterScreen(navController = nav)
}
