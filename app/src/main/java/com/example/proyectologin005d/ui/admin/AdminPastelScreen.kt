package com.example.proyectologin005d.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectologin005d.data.model.Pastel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPastelScreen(
    viewModel: AdminPastelViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var pastelEditando by remember { mutableStateOf<Pastel?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar catálogo de tortas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                },
                actions = {
                    // RESET catálogo
                    TextButton(onClick = { viewModel.resetCatalog() }) {
                        Text("Reset")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    pastelEditando = null
                    showDialog = true
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.lista) { pastel ->
                        PastelAdminItem(
                            pastel = pastel,
                            onEditar = {
                                pastelEditando = pastel
                                showDialog = true
                            },
                            onEliminar = { viewModel.eliminarPastel(pastel.codigo) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        PastelCrudDialog(
            pastel = pastelEditando,
            onDismiss = { showDialog = false },
            onSave = { nuevo ->
                if (pastelEditando == null)
                    viewModel.crearPastel(nuevo)
                else
                    viewModel.actualizarPastel(nuevo)

                showDialog = false
            }
        )
    }
}

@Composable
fun PastelAdminItem(
    pastel: Pastel,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(pastel.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Código: ${pastel.codigo}")
            Text("Categoría: ${pastel.categoria}")
            Text("Precio: $${pastel.precio}")
            Text("Stock: ${pastel.stock}")

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onEditar) { Text("Editar") }
                OutlinedButton(onClick = onEliminar) { Text("Eliminar") }
            }
        }
    }
}

@Composable
fun PastelCrudDialog(
    pastel: Pastel?,
    onDismiss: () -> Unit,
    onSave: (Pastel) -> Unit
) {
    val esEdicion = pastel != null

    var codigo by remember(pastel) { mutableStateOf(pastel?.codigo ?: "") }
    var nombre by remember(pastel) { mutableStateOf(pastel?.nombre ?: "") }
    var categoria by remember(pastel) { mutableStateOf(pastel?.categoria ?: "") }
    var precioText by remember(pastel) { mutableStateOf(pastel?.precio?.toString() ?: "") }
    var stockText by remember(pastel) { mutableStateOf(pastel?.stock?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (esEdicion) "Editar pastel" else "Crear pastel") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { if (!esEdicion) codigo = it },
                    label = { Text("Código") },
                    singleLine = true,
                    enabled = !esEdicion
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = precioText,
                    onValueChange = { precioText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Precio") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Stock") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val precio = precioText.toIntOrNull() ?: 0
                    val stock = stockText.toIntOrNull() ?: 0

                    if (codigo.isNotBlank() && nombre.isNotBlank()) {
                        onSave(
                            Pastel(
                                codigo = codigo,
                                nombre = nombre,
                                categoria = categoria,
                                precio = precio,
                                stock = stock,
                                imagen = pastel?.imagen,
                                descripcion = pastel?.descripcion
                            )
                        )
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
