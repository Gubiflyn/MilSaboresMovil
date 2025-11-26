@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.proyectologin005d.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectologin005d.R
import kotlinx.coroutines.delay

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
                    onBack = { },
                    onCart = { navController.navigate("cart") },
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

                // Banner superior
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

                // Carrusel de tortas
                TortasCarousel(brown = Brown)

                Text(
                    "Destacados de hoy",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextMain
                )

                // Tarjetas destacadas (usan FeaturedCard, que ahora está en otro archivo)
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

@Composable
private fun HomeTopBar(
    onBack: () -> Unit,
    onCart: () -> Unit,
    brown: Color
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Mil Sabores",
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 12.dp)
            )
        },
        actions = {
            TextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar",
                        tint = brown
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Transparent,
                    focusedTextColor = Color.Transparent,
                    unfocusedTextColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color.Transparent
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
private fun TortasCarousel(brown: Color) {
    val tortaImages = listOf(
        R.drawable.tor_frutas,
        R.drawable.tor_cump,
        R.drawable.tor_vainilla
    )

    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % tortaImages.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = tortaImages[currentIndex]),
                contentDescription = "Torta destacada",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            tortaImages.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentIndex) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == currentIndex) brown
                            else Color(0xFFDDC4A3)
                        )
                )
                Spacer(Modifier.width(6.dp))
            }
        }
    }
}
