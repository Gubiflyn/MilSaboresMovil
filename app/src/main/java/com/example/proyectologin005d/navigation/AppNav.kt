package com.example.proyectologin005d.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.proyectologin005d.login.LoginScreen
import com.example.proyectologin005d.register.RegisterScreen
import com.example.proyectologin005d.ui.product.DetailScreen
import com.example.proyectologin005d.ui.cart.CartScreen
import com.example.proyectologin005d.ui.home.HomeScreen
import com.example.proyectologin005d.ui.catalog.CatalogScreen   // ← asegúrate que este package coincida aaaaaaaaa
import com.example.proyectologin005d.ui.auth.AuthViewModel
import com.example.proyectologin005d.ui.cart.CartViewModel
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.home.HomeViewModelFactory

import com.example.proyectologin005d.ui.profile.ProfileScreen

@Composable
fun AppNav() {
    val nav = rememberNavController()

    val authVm: AuthViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    val appCtx = LocalContext.current.applicationContext
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(appCtx))

    val user by authVm.user.collectAsState()

    LaunchedEffect(user) {
        cartVm.setUser(user)
    }

    NavHost(navController = nav, startDestination = "login") {

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

        composable("register") {
            RegisterScreen(navController = nav)
        }

        composable("home") {
            HomeScreen(navController = nav, viewModel = homeVm)
        }

        composable("catalog") {
            CatalogScreen(navController = nav, viewModel = homeVm)
        }

        composable("profile") {
            ProfileScreen(
                navController = nav,
                authViewModel = authVm
            )
        }

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

        composable("cart") {
            CartScreen(
                vm = cartVm,
                onPaid = {
                    cartVm.clear()
                    nav.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
