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

import com.example.proyectologin005d.ui.auth.AuthViewModel
import com.example.proyectologin005d.ui.cart.CartViewModel
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.home.HomeViewModelFactory

import com.example.proyectologin005d.ui.common.HomeScreenWithFooter
import com.example.proyectologin005d.ui.common.CatalogScreenWithFooter
import com.example.proyectologin005d.ui.common.ProfileScreenWithFooter
import com.example.proyectologin005d.ui.common.CartScreenWithFooter
import com.example.proyectologin005d.ui.common.DetailScreenWithFooter
import com.example.proyectologin005d.ui.common.HistoryScreenWithFooter

@Composable
fun AppNav() {
    val nav = rememberNavController()

    // ViewModels compartidos
    val authVm: AuthViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    // HomeViewModel con factory (tu proyecto lo usa así)
    val appCtx = LocalContext.current.applicationContext
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(appCtx))

    val user by authVm.user.collectAsState()

    // Alinear carrito con usuario
    LaunchedEffect(user) { cartVm.setUser(user) }

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
        composable("register") { RegisterScreen(navController = nav) }

        // HOME + FOOTER
        composable("home") { HomeScreenWithFooter(navController = nav, vm = homeVm) }

        // CATALOGO + FOOTER
        composable("catalog") { CatalogScreenWithFooter(navController = nav, vm = homeVm) }

        // DETALLE + FOOTER
        composable(
            route = "detail/{codigo}",
            arguments = listOf(navArgument("codigo") { type = NavType.StringType })
        ) { backStackEntry ->
            val codigo = backStackEntry.arguments?.getString("codigo").orEmpty()
            DetailScreenWithFooter(
                navController = nav,
                codigo = codigo,
                onAddToCart = { nombre, precio ->
                    cartVm.add(nombre, precio)
                    nav.navigate("cart")
                }
            )
        }

        // CARRITO + FOOTER
        composable("cart") {
            CartScreenWithFooter(
                navController = nav,
                vm = cartVm,
                onPaid = { nav.navigate("history") { launchSingleTop = true } }
            )
        }

        // PERFIL + FOOTER
        composable("profile") { ProfileScreenWithFooter(navController = nav, authVm = authVm) }

        // HISTORIAL + FOOTER
        composable("history") { HistoryScreenWithFooter(navController = nav) }
    }

    // Logout → vuelve a login
    LaunchedEffect(user) {
        if (user == null) {
            nav.navigate("login") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
