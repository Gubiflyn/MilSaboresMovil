package com.example.proyectologin005d.ui.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectologin005d.ui.home.HomeViewModel
import androidx.compose.ui.draw.clip
import com.example.proyectologin005d.ui.home.HomeBottomBar   // 👈 reutilizamos la barra inferior

// Orden EXACTO según tu documento
private val ORDERED_CATEGORIES = listOf(
    "Tortas Cuadradas",
    "Tortas Circulares",
    "Postres Individuales",
    "Productos Sin Azúcar",
    "Pastelería Tradicional",
    "Productos sin gluten",
    "Productos Vegana",
    "Tortas Especiales"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    // Limpia cualquier filtro previo
    LaunchedEffect(Unit) { viewModel.setCategoria(null) }

    val ui by viewModel.ui.collectAsState()

    // Paleta (clarita)
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)
    val CardBg = Color(0xFFFFE6C7)    // beige pastel
    val TextMain = Color(0xFF3B2A1A)  // café oscuro
    val TextSub = Color(0xFF4B3621)   // café medio

    // Ordena por categoría y luego por nombre
    val listaOrdenada = remember(ui.items) {
        ui.items.sortedWith(
            compareBy(
                { ORDERED_CATEGORIES.indexOf(it.categoria).let { idx -> if (idx == -1) Int.MAX_VALUE else idx } },
                { it.nombre }
            )
        )
    }

    // Agrupa por categoría (sólo las que tienen productos)
    val porCategoria = remember(listaOrdenada) {
        ORDERED_CATEGORIES.associateWith { cat -> listaOrdenada.filter { it.categoria == cat } }
            .filterValues { it.isNotEmpty() }
    }

    // Usa esquema CLARO en esta pantalla
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Brown,
            onPrimary = Color.White,
            surface = Cream,
            onSurface = TextMain
        )
    ) {
        Scaffold(
            containerColor = Cream,
            topBar = {
                TopAppBar(
                    title = { Text("Catálogo") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Brown)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Brown
                    )
                )
            },
            bottomBar = {
                HomeBottomBar(
                    current = "catalog",                       // 👈 marca esta pestaña
                    onHome = { navController.navigate("home") },
                    onSearch = { /* ya estás en catálogo */ },  // o navController.navigate("catalog")
                    onHistory = { /* navController.navigate("history") */ },
                    onProfile = { navController.navigate("profile") },
                    brown = Brown
                )
            }
        ) { padding ->

            if (ui.loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Cream),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    porCategoria.forEach { (categoria, productos) ->

                        // Encabezado de categoría
                        item {
                            Surface(color = Cream) {
                                Text(
                                    text = "🍰  $categoria",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Brown,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Brown.copy(alpha = 0.08f))
                                        .padding(vertical = 8.dp, horizontal = 8.dp)
                                )
                            }
                        }

                        // Items
                        items(productos, key = { it.codigo }) { p ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = CardBg,
                                    contentColor = TextMain
                                ),
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                // 🔹 Resolver id del drawable de forma simple (por p.imagen)
                                val ctx = LocalContext.current
                                val imgId = remember(p.imagen) {
                                    p.imagen?.let { name ->
                                        ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
                                    } ?: 0
                                }

                                ListItem(
                                    leadingContent = {
                                        if (imgId != 0) {
                                            Image(
                                                painter = painterResource(imgId),
                                                contentDescription = p.nombre,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(56.dp)
                                                    .clip(MaterialTheme.shapes.medium)
                                            )
                                        }
                                    },
                                    colors = ListItemDefaults.colors(
                                        containerColor = Color.Transparent,
                                        headlineColor = TextMain,
                                        supportingColor = TextSub,
                                        trailingIconColor = Brown
                                    ),
                                    headlineContent = {
                                        Text(
                                            p.nombre,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    supportingContent = {
                                        Text("${p.categoria.uppercase()} • $${p.precio}")
                                    },
                                    trailingContent = {
                                        TextButton(onClick = {
                                            navController.navigate("detail/${p.codigo}")
                                        }) {
                                            Text("Ver", color = Brown, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                )
                            }
                        }

                        // Separación entre categorías
                        item { Spacer(Modifier.height(12.dp)) }
                    }

                    if (porCategoria.isEmpty()) {
                        item {
                            Text(
                                "No hay productos disponibles.",
                                color = TextMain,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
