package com.example.proyectologin005d.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.proyectologin005d.login.LoginScreen
import com.example.proyectologin005d.register.RegisterScreen

import com.example.proyectologin005d.ui.home.HomeScreen
import com.example.proyectologin005d.ui.catalog.CatalogScreen
import com.example.proyectologin005d.ui.product.DetailScreen
import com.example.proyectologin005d.ui.cart.CartScreen
import com.example.proyectologin005d.ui.profile.ProfileScreen
import com.example.proyectologin005d.ui.history.HistoryScreen

import com.example.proyectologin005d.ui.auth.AuthViewModel
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.home.HomeViewModelFactory
import com.example.proyectologin005d.ui.cart.CartViewModel

@Composable
fun AppNav() {
    val nav = rememberNavController()

    // ViewModels compartidos en navegación
    val authVm: AuthViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    // HomeViewModel requiere un Factory en tu proyecto
    val appCtx = LocalContext.current.applicationContext
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(appCtx))

    val user by authVm.user.collectAsState()

    // Mantener carrito alineado al usuario (si tu CartVM lo ocupa)
    LaunchedEffect(user) {
        cartVm.setUser(user)
    }

    NavHost(
        navController = nav,
        startDestination = if (user == null) "login" else "home"
    ) {
        // LOGIN
        composable("login") {
            LoginScreen(
                navController = nav,
                onLoginSuccess = { u ->
                    authVm.setUser(u)
                    nav.navigate("home") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // REGISTER
        composable("register") {
            RegisterScreen(navController = nav)
        }

        // HOME (requiere viewModel)
        composable("home") {
            HomeScreen(navController = nav, viewModel = homeVm)
        }

        // CATÁLOGO (requiere viewModel)
        composable("catalog") {
            CatalogScreen(navController = nav, viewModel = homeVm)
        }

        // DETALLE (requiere 'codigo' y callback onAddToCart)
        composable(
            route = "detail/{codigo}",
            arguments = listOf(navArgument("codigo") { type = NavType.StringType })
        ) { backStackEntry ->
            val codigo = backStackEntry.arguments?.getString("codigo").orEmpty()
            DetailScreen(
                navController = nav,
                codigo = codigo,
                onAddToCart = { nombre, precio ->
                    cartVm.add(nombre, precio)
                    nav.navigate("cart")
                }
            )
        }

        // CARRITO (usa el CartViewModel compartido)
        composable("cart") {
            CartScreen(
                vm = cartVm,
                onPaid = {
                    // Navega al historial apenas se complete placeOrder()
                    nav.navigate("history") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // PERFIL (requiere authViewModel)
        composable("profile") {
            ProfileScreen(navController = nav, authViewModel = authVm)
        }

        // HISTORIAL (tu HistoryScreen NO recibe parámetros)
        composable("history") {
            HistoryScreen()
        }
    }

    // Si cambia el usuario a null (logout), vuelve al login
    LaunchedEffect(user) {
        if (user == null) {
            nav.navigate("login") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
