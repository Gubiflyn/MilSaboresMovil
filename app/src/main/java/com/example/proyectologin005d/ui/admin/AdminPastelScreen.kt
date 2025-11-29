package com.example.proyectologin005d.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar catálogo de tortas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // En vez de Icon + Icons.Default, usamos solo texto
                        Text(text = "←")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Text(text = "⟳")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Aquí luego abriremos el diálogo de "Agregar pastel"
                    // Por ahora solo de ejemplo
                    // TODO: conectar con un formulario
                }
            ) {
                Text(text = "+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    state.error?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.lista) { pastel ->
                            PastelAdminItem(
                                pastel = pastel,
                                onEditar = {
                                    // TODO: abrir diálogo de edición con datos del pastel
                                },
                                onEliminar = {
                                    viewModel.eliminarPastel(pastel.codigo)
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pastel.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Código: ${pastel.codigo}")
            Text(text = "Categoría: ${pastel.categoria}")
            Text(text = "Precio: $${pastel.precio}")
            Text(text = "Stock: ${pastel.stock}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onEditar) {
                    Text("Editar")
                }
                OutlinedButton(onClick = onEliminar) {
                    Text("Eliminar")
                }
            }
        }
    }
}