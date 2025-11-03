package com.example.proyectologin005d.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectologin005d.data.database.PastelDatabase
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.repository.PastelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    codigo: String,
    onAddToCart: (nombre: String, precio: Int) -> Unit
) {
    val ctx = LocalContext.current
    var pastel by remember { mutableStateOf<Pastel?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(codigo) {
        try {
            val repo = PastelRepository(PastelDatabase.getInstance(ctx).pastelDao())
            pastel = withContext(Dispatchers.IO) { repo.getByCodigo(codigo) }
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when {
                loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
                error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
                pastel == null -> Text("Producto no encontrado.")
                else -> {
                    val imgId = remember(pastel!!.imagen) {
                        pastel!!.imagen?.let { name ->
                            ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
                        } ?: 0
                    }
                    if (imgId != 0) {
                        Image(
                            painter = painterResource(imgId),
                            contentDescription = pastel!!.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    Text(pastel!!.nombre, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(6.dp))
                    Text("Código: ${pastel!!.codigo}")
                    Text("Categoría: ${pastel!!.categoria}")
                    Spacer(Modifier.height(6.dp))
                    pastel!!.descripcion?.let {
                        Text(it)
                        Spacer(Modifier.height(12.dp))
                    }
                    Text("Precio: $${pastel!!.precio}", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val p = pastel ?: return@Button
                            onAddToCart(p.nombre, p.precio)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Agregar al carrito") }
                }
            }
        }
    }
}
