package com.example.proyectologin005d.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.proyectologin005d.ui.cart.CartScreen
import com.example.proyectologin005d.ui.cart.CartViewModel
import com.example.proyectologin005d.ui.history.HistoryScreen
import com.example.proyectologin005d.ui.product.DetailScreen
import com.example.proyectologin005d.ui.catalog.CatalogScreen
import com.example.proyectologin005d.ui.home.HomeScreen
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.profile.ProfileScreen
import com.example.proyectologin005d.ui.auth.AuthViewModel
import androidx.compose.ui.graphics.Color


private val Brown = Color(0xFF8B4513)
private val Cream = Color(0xFFFFF5E1)
private val TextMain = Color(0xFF3B2A1A)

@Composable
fun themeContent(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Brown,
            onPrimary = Color.White,
            surface = Cream,
            background = Cream,
            onSurface = TextMain
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            content()
        }
    }
}

@Composable
fun HomeScreenWithFooter(
    navController: NavController,
    vm: HomeViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppFooter(navController) }
    ) { padding: PaddingValues ->
        themeContent {
            Box(Modifier.fillMaxSize().padding(padding)) {
                HomeScreen(navController = navController, viewModel = vm)
            }
        }
    }
}

@Composable
fun CatalogScreenWithFooter(
    navController: NavController,
    vm: HomeViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppFooter(navController) }
    ) { padding: PaddingValues ->
        themeContent {
            Box(Modifier.fillMaxSize().padding(padding)) {
                CatalogScreen(navController = navController, viewModel = vm)
            }
        }
    }
}

@Composable
fun ProfileScreenWithFooter(
    navController: NavController,
    authVm: AuthViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppFooter(navController) }
    ) { padding: PaddingValues ->
        themeContent {
            Box(Modifier.fillMaxSize().padding(padding)) {
                ProfileScreen(navController = navController, authViewModel = authVm)
            }
        }
    }
}


@Composable
fun CartScreenWithFooter(
    navController: NavController,
    vm: CartViewModel,
    onPaid: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppFooter(navController) }
    ) { padding: PaddingValues ->
        themeContent {
            Box(Modifier.fillMaxSize().padding(padding)) {
                CartScreen(vm = vm, onPaid = onPaid)
            }
        }
    }
}

@Composable
fun DetailScreenWithFooter(
    navController: NavController,
    codigo: String,
    onAddToCart: (String, Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppFooter(navController) }
    ) { padding: PaddingValues ->
        themeContent {
            Box(Modifier.fillMaxSize().padding(padding)) {
                DetailScreen(
                    navController = navController,
                    codigo = codigo,
                    onAddToCart = onAddToCart
                )
            }
        }
    }
}

@Composable
fun HistoryScreenWithFooter(
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppFooter(navController) }
    ) { padding: PaddingValues ->
        themeContent {
            Box(Modifier.fillMaxSize().padding(padding)) {
                HistoryScreen()
            }
        }
    }
}
