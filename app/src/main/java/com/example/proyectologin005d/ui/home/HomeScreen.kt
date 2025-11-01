@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.proyectologin005d.ui.home

// ====== IMPORTS ======
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectologin005d.R
import androidx.compose.material3.ExperimentalMaterial3Api


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel // se mantiene para compatibilidad, aquí no se usa
) {
    // Paleta que ya usabas
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Brown,
            onPrimary = Color.White,
            onSurface = Color(0xFF333333)
        )
    ) {
        androidx.compose.material3.Scaffold(
            containerColor = Cream,
            topBar = {
                HomeTopBar(
                    onBack = { /* TODO: acción atrás */ },
                    onCart = { /* navController.navigate("cart") */ },
                    brown = Brown
                )
            },
            bottomBar = {
                HomeBottomBar(
                    current = "home",
                    onHome = { /* ya estás en home */ },
                    onSearch = { /* navController.navigate("buscar") */ },
                    onHistory = { /* navController.navigate("historial") */ },
                    onProfile = { /* navController.navigate("perfil") */ },
                    brown = Brown
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Cream)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ======= Botón para ir al Catálogo =======
                item {
                    Button(
                        onClick = { navController.navigate("catalog") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Brown)
                    ) { Text("Ver catálogo", fontWeight = FontWeight.Bold) }
                }

                // ======= Banner superior =======
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tortashome),
                            contentDescription = "Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // ======= Botón “Promociones del día” =======
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { /* TODO: ir a promociones */ },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brown),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(36.dp)
                        ) {
                            Text("Promociones del día", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // 🚫 NO mostramos listado ni chips en Home (queda limpio)
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

/* ======================= Componentes UI ======================= */

@Composable
private fun HomeTopBar(
    onBack: () -> Unit,
    onCart: () -> Unit,
    brown: Color
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = brown)
            }
        },
        actions = {
            TextField(
                value = "",
                onValueChange = {},
                enabled = false,
                placeholder = { Text("Selecciona el local más cercano") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = Color(0x99000000),
                    disabledPlaceholderColor = Color(0x99000000)
                ),
                modifier = Modifier
                    .width(220.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            IconButton(onClick = onCart) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Carrito",
                    tint = brown
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = brown
        )
    )
}

@Composable
private fun ProductCard( // opcional si más adelante quieres “destacados”
    title: String,
    price: String,
    image: Int,
    brown: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = image),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(brown.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏷", fontSize = 12.sp)
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(text = price, fontWeight = FontWeight.Bold, color = brown)
                }
            }
        }
    }
}

@Composable
private fun HomeBottomBar(
    current: String,
    onHome: () -> Unit,
    onSearch: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    brown: Color
) {
    NavigationBar(
        containerColor = brown,
        tonalElevation = 4.dp
    ) {
        val selectedColor = Color.White

        NavigationBarItem(
            selected = current == "home",
            onClick = onHome,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home", tint = selectedColor) },
            label = { Text("HOME", color = selectedColor) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = selectedColor.copy(alpha = 0.6f),
                unselectedTextColor = selectedColor.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
        NavigationBarItem(
            selected = current == "search",
            onClick = onSearch,
            icon = { Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = selectedColor) },
            label = { Text("Buscar", color = selectedColor) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = selectedColor.copy(alpha = 0.6f),
                unselectedTextColor = selectedColor.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
        NavigationBarItem(
            selected = current == "history",
            onClick = onHistory,
            icon = { Icon(Icons.Filled.History, contentDescription = "Historial", tint = selectedColor) },
            label = { Text("Mi historial", color = selectedColor) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = selectedColor.copy(alpha = 0.6f),
                unselectedTextColor = selectedColor.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
        NavigationBarItem(
            selected = current == "profile",
            onClick = onProfile,
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil", tint = selectedColor) },
            label = { Text("Mi Perfil", color = selectedColor) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = selectedColor.copy(alpha = 0.6f),
                unselectedTextColor = selectedColor.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
    }
}



