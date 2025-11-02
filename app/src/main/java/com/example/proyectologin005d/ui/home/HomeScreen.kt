@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.proyectologin005d.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectologin005d.R

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)
    val CardBg = Color(0xFFFFE6C7)
    val TextMain = Color(0xFF3B2A1A)

    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme(
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
                    onCart = { navController.navigate("cart") },
                    brown = Brown
                )
            },
            bottomBar = {
                HomeBottomBar(
                    current = "home",
                    onHome = { /* ya estás en home */ },
                    onSearch = { navController.navigate("catalog") },
                    onHistory = { /* navController.navigate("historial") */ },
                    onProfile = { navController.navigate("profile") },
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

                /* ---------- Hero banner con overlay ---------- */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tortashome),
                        contentDescription = "Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0x80000000))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Text(
                            "Mil Sabores",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Tortas y postres recién hechos 🍰",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
                }



                /* ---------- Destacados de hoy ---------- */
                Text(
                    "Destacados de hoy",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextMain
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FeaturedCard(
                        title = "Torta Cuadrada de Chocolate",
                        desc = "Bizcocho húmedo, ganache intensa",
                        bg = CardBg,
                        fg = TextMain
                    ) { navController.navigate("catalog") }

                    FeaturedCard(
                        title = "Postre Tres Leches",
                        desc = "Clásico y cremoso, porción individual",
                        bg = CardBg,
                        fg = TextMain
                    ) { navController.navigate("catalog") }
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
fun HomeBottomBar(
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
            selected = current == "catalog",
            onClick = onSearch,
            icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Catálogo", tint = sel) },
            label = { Text("Catálogo", color = sel) },
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

/* ----- Componentes pequeños reutilizables ----- */

@Composable
private fun QuickAction(
    title: String,
    subtitle: String,
    icon: ImageVector,
    bg: Color,
    fg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(88.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(fg.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = fg)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, color = fg, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = fg.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun FeaturedCard(
    title: String,
    desc: String,
    bg: Color,
    fg: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, color = fg, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(desc, color = fg.copy(alpha = 0.9f), fontSize = 13.sp)
            }
            Icon(
                imageVector = Icons.Filled.ListAlt,
                contentDescription = "Ver",
                tint = fg.copy(alpha = 0.9f)
            )
        }
    }
}
