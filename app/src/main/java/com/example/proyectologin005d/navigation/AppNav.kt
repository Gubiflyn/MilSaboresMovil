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
import com.example.proyectologin005d.ui.catalog.CatalogScreen

import com.example.proyectologin005d.ui.auth.AuthViewModel
import com.example.proyectologin005d.ui.cart.CartViewModel
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.home.HomeViewModelFactory

import com.example.proyectologin005d.ui.profile.ProfileScreen

import com.example.proyectologin005d.ui.history.HistoryScreen
import com.example.proyectologin005d.ui.history.HistoryViewModel
import com.example.proyectologin005d.ui.history.HistoryViewModelFactory

import com.example.proyectologin005d.data.repository.OrderRepository
// ⬇️ Usa el nombre real de tu DB (coincide con el archivo que enviaste)
import com.example.proyectologin005d.data.database.PastelDatabase

@Composable
fun AppNav() {
    val nav = rememberNavController()

    val authVm: AuthViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    val appCtx = LocalContext.current.applicationContext
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(appCtx))

    // ⬇️ Crea el repo de órdenes una sola vez e inyéctalo al CartViewModel
    val orderRepo by remember {
        mutableStateOf(
            OrderRepository(PastelDatabase.getInstance(appCtx).orderDao())
        )
    }
    LaunchedEffect(Unit) {
        cartVm.setOrderRepository(orderRepo)
    }

    val user by authVm.user.collectAsState()

    LaunchedEffect(user) {
        cartVm.setUser(user)
    }

    // ⬇️ VM de historial (usa el mismo AuthViewModel para leer el usuario actual)
    val historyVm: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(appCtx, authVm))

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

        // ⬇️ Ruta del historial (lee del HistoryViewModel ya creado arriba)
        composable("history") {
            HistoryScreen(navController = nav, vm = historyVm)
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
                    // ⬇️ Guarda la orden con el email del usuario y luego navega
                    cartVm.placeOrder(user?.email)

                    nav.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
