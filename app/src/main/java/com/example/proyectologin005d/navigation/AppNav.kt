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

// Wrappers con footer/tema
import com.example.proyectologin005d.ui.common.HomeScreenWithFooter
import com.example.proyectologin005d.ui.common.CatalogScreenWithFooter
import com.example.proyectologin005d.ui.common.DetailScreenWithFooter
import com.example.proyectologin005d.ui.common.CartScreenWithFooter
import com.example.proyectologin005d.ui.common.ProfileScreenWithFooter
import com.example.proyectologin005d.ui.common.HistoryScreenWithFooter

// VMs
import com.example.proyectologin005d.ui.auth.AuthViewModel
import com.example.proyectologin005d.ui.cart.CartViewModel
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.home.HomeViewModelFactory

// DB/Repo del historial
import com.example.proyectologin005d.data.database.PastelDatabase
import com.example.proyectologin005d.data.repository.OrderRepository

// Pantallas de animación
import com.example.proyectologin005d.ui.login.LoginAnimationScreen
import com.example.proyectologin005d.ui.login.GuestAnimationScreen
import com.example.proyectologin005d.ui.cart.PurchaseSuccessScreen

@Composable
fun AppNav() {
    val nav = rememberNavController()

    val authVm: AuthViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    val appCtx = LocalContext.current.applicationContext
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(appCtx))

    val user by authVm.user.collectAsState()

    // Inyectar OrderRepository al CartViewModel
    LaunchedEffect(Unit) {
        val db = PastelDatabase.getInstance(appCtx)
        val orderRepo = OrderRepository(db.orderDao())
        cartVm.setOrderRepository(orderRepo)
    }

    // Mantener carrito alineado al usuario
    LaunchedEffect(user) {
        cartVm.setUser(user)
    }

    NavHost(
        navController = nav,
        // Para probar la animación siempre partimos en login
        startDestination = "login"
    ) {
        // LOGIN
        composable("login") {
            LoginScreen(
                navController = nav,
                onLoginSuccess = { u ->
                    // Guardamos usuario globalmente
                    authVm.setUser(u)
                    // NAVEGACIÓN A LA ANIMACIÓN SE HACE EN LoginScreen
                }
            )
        }

        // LOGIN ANIMATION
        composable("login_animation") {
            LoginAnimationScreen(navController = nav)
        }

        // GUEST ANIMATION
        composable("guest_animation") {
            GuestAnimationScreen(navController = nav)
        }

        // REGISTER
        composable("register") {
            RegisterScreen(navController = nav)
        }

        // HOME
        composable("home") {
            HomeScreenWithFooter(navController = nav, vm = homeVm)
        }

        // CATÁLOGO
        composable("catalog") {
            CatalogScreenWithFooter(navController = nav, vm = homeVm)
        }

        // DETALLE
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

        // CARRITO
        composable("cart") {
            CartScreenWithFooter(
                navController = nav,
                vm = cartVm,
                onPaid = {
                    // 1) Guardamos la orden ANTES de mostrar la animación
                    cartVm.placeOrder(userEmail = user?.email)

                    // 2) Vamos a la pantalla de "Compra exitosa"
                    nav.navigate("purchase_success") {
                        popUpTo("cart") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🆕 COMPRA EXITOSA
        composable("purchase_success") {
            PurchaseSuccessScreen(navController = nav)
        }

        // PERFIL
        composable("profile") {
            ProfileScreenWithFooter(navController = nav, authVm = authVm)
        }

        // HISTORIAL
        composable("history") {
            HistoryScreenWithFooter(navController = nav)
        }
    }

    // Logout → Login (lo dejamos por si usas cerrar sesión en otra parte)
    LaunchedEffect(user) {
        if (user == null) {
            nav.navigate("login") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}




