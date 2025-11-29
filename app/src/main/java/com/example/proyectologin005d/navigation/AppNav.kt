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
import com.example.proyectologin005d.ui.login.GuestAnimationScreen
import com.example.proyectologin005d.ui.login.LoginAnimationScreen
import com.example.proyectologin005d.ui.cart.PurchaseSuccessScreen

// --- Admin: pantalla + VM + factory ---
import com.example.proyectologin005d.ui.admin.AdminPastelScreen
import com.example.proyectologin005d.ui.admin.AdminPastelViewModel
import com.example.proyectologin005d.ui.admin.AdminPastelViewModelFactory

// --- Ejemplo API externa: JSONPlaceholder ---
import com.example.proyectologin005d.ui.jsonplaceholder.JsonPlaceholderScreen
import com.example.proyectologin005d.ui.jsonplaceholder.JsonPlaceholderViewModel

@Composable
fun AppNav() {
    val nav = rememberNavController()

    val authVm: AuthViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    val appCtx = LocalContext.current.applicationContext
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(appCtx))

    val user by authVm.user.collectAsState()

    LaunchedEffect(Unit) {
        val db = PastelDatabase.getInstance(appCtx)
        val orderRepo = OrderRepository(db.orderDao())
        cartVm.setOrderRepository(orderRepo)
    }

    LaunchedEffect(user) {
        cartVm.setUser(user)
    }

    NavHost(
        navController = nav,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = nav,
                onLoginSuccess = { u ->
                    authVm.setUser(u)
                }
            )
        }

        composable("login_animation") {
            LoginAnimationScreen(navController = nav)
        }

        composable("guest_animation") {
            GuestAnimationScreen(navController = nav)
        }

        composable("register") {
            RegisterScreen(navController = nav)
        }

        // -------- ADMIN: panel CRUD de pasteles --------
        composable("admin_pasteles") {
            val adminVm: AdminPastelViewModel = viewModel(
                factory = AdminPastelViewModelFactory(appCtx)
            )

            AdminPastelScreen(
                viewModel = adminVm,
                onBack = {
                    nav.popBackStack()
                },
                onOpenJsonPlaceholder = {
                    nav.navigate("jsonplaceholder")
                }
            )
        }

        // -------- Ejemplo API externa: JSONPlaceholder --------
        composable("jsonplaceholder") {
            val jsonVm: JsonPlaceholderViewModel = viewModel()
            JsonPlaceholderScreen(
                viewModel = jsonVm,
                onBack = { nav.popBackStack() }
            )
        }

        composable("home") {
            HomeScreenWithFooter(navController = nav, vm = homeVm)
        }

        composable("catalog") {
            CatalogScreenWithFooter(navController = nav, vm = homeVm)
        }

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

        composable("cart") {
            CartScreenWithFooter(
                navController = nav,
                vm = cartVm,
                onPaid = {
                    cartVm.placeOrder(userEmail = user?.email)

                    nav.navigate("purchase_success") {
                        popUpTo("cart") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("purchase_success") {
            PurchaseSuccessScreen(navController = nav)
        }

        composable("profile") {
            ProfileScreenWithFooter(navController = nav, authVm = authVm)
        }

        composable("history") {
            HistoryScreenWithFooter(navController = nav)
        }
    }

    LaunchedEffect(user) {
        if (user == null) {
            nav.navigate("login") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
