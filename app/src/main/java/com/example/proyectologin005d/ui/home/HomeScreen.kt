@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.proyectologin005d.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
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

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel // si no lo usas aún, lo dejamos para mantener firma
) {
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Brown,
            onPrimary = Color.White,
            onSurface = Color(0xFF333333)
        )
    ) {
        Scaffold(
            containerColor = Cream,
            topBar = {
                HomeTopBar(
                    onBack = { /* si luego quieres manejar back */ },
                    onCart = { navController.navigate("cart") },   // ← sin Routes
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Cream)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("catalog") }, // ← sin Routes
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Brown)
                ) { Text("Ver catálogo", fontWeight = FontWeight.Bold) }

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

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { /* navController.navigate("promos") */ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Brown),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(36.dp)
                    ) {
                        Text("Promociones del día", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/* ============== UI helpers ============== */

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
private fun HomeBottomBar(
    current: String,
    onHome: () -> Unit,
    onSearch: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    brown: Color
) {
    NavigationBar(containerColor = brown, tonalElevation = 4.dp) {
        val sel = Color.White

        NavigationBarItem(
            selected = current == "home",
            onClick = onHome,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home", tint = sel) },
            label = { Text("HOME", color = sel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = sel,
                selectedTextColor = sel,
                unselectedIconColor = sel.copy(alpha = 0.6f),
                unselectedTextColor = sel.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
        NavigationBarItem(
            selected = current == "search",
            onClick = onSearch,
            icon = { Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = sel) },
            label = { Text("Buscar", color = sel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = sel,
                selectedTextColor = sel,
                unselectedIconColor = sel.copy(alpha = 0.6f),
                unselectedTextColor = sel.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
        NavigationBarItem(
            selected = current == "history",
            onClick = onHistory,
            icon = { Icon(Icons.Filled.History, contentDescription = "Historial", tint = sel) },
            label = { Text("Mi historial", color = sel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = sel,
                selectedTextColor = sel,
                unselectedIconColor = sel.copy(alpha = 0.6f),
                unselectedTextColor = sel.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
        NavigationBarItem(
            selected = current == "profile",
            onClick = onProfile,
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil", tint = sel) },
            label = { Text("Mi Perfil", color = sel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = sel,
                selectedTextColor = sel,
                unselectedIconColor = sel.copy(alpha = 0.6f),
                unselectedTextColor = sel.copy(alpha = 0.6f),
                indicatorColor = brown
            )
        )
    }
}
