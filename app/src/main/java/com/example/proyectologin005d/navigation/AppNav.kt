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
import com.example.proyectologin005d.view.DrawerMenu
import com.example.proyectologin005d.ui.auth.AuthViewModel
import com.example.proyectologin005d.ui.cart.CartScreen
import com.example.proyectologin005d.ui.cart.CartViewModel
import com.example.proyectologin005d.ui.catalog.CatalogScreen
import com.example.proyectologin005d.ui.home.HomeScreen
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.home.HomeViewModelFactory
import com.example.proyectologin005d.ui.home.MuestraDatosScreen
import com.example.proyectologin005d.ui.product.DetailScreen

@Composable
fun AppNav() {
    val nav = rememberNavController()

    // VMs
    val appCtx = LocalContext.current.applicationContext
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(appCtx))
    val cartVm: CartViewModel = viewModel()
    val authVm: AuthViewModel = viewModel()

    // Si tu AuthViewModel expone el usuario, sincroniza con el carrito
    val user by authVm.user.collectAsState()
    LaunchedEffect(user) { cartVm.setUser(user) }

    NavHost(navController = nav, startDestination = Routes.LOGIN) {

        // LOGIN
        composable(Routes.LOGIN) {
            LoginScreen(navController = nav)
        }

        // REGISTER
        composable(Routes.REGISTER) {
            RegisterScreen(navController = nav)
        }

        // DRAWER (sin y con username)
        composable(Routes.DRAWER) {
            DrawerMenu(navController = nav)
        }
        composable(
            route = Routes.DRAWER_WITH_USERNAME,
            arguments = listOf(navArgument(Args.USERNAME) { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString(Args.USERNAME).orEmpty()
            DrawerMenu(username = username, navController = nav)
        }

        // HOME
        composable(Routes.HOME) {
            HomeScreen(navController = nav, viewModel = homeVm)
        }

        // CATÁLOGO
        composable(Routes.CATALOG) {
            CatalogScreen(navController = nav, viewModel = homeVm)
        }

        // Muestra datos (si la usas)
        composable(Routes.MUESTRA_DATOS) {
            MuestraDatosScreen(navController = nav, username = "Invitado")
        }

        // DETALLE (ahora PASAMOS onAddToCart)
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument(Args.CODIGO) { type = NavType.StringType })
        ) { backStackEntry ->
            val codigo = backStackEntry.arguments?.getString(Args.CODIGO).orEmpty()
            DetailScreen(
                navController = nav,
                codigo = codigo,
                onAddToCart = { nombre, precio ->
                    cartVm.add(nombre, precio)
                    nav.navigate(Routes.CART)
                }
            )
        }

        // CARRITO
        composable(Routes.CART) {
            CartScreen(
                vm = cartVm,
                onPaid = { nav.popBackStack(Routes.HOME, inclusive = false) }
            )
        }
    }
}

/* ======== Rutas y Args ======== */
private object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DRAWER = "DrawerMenu"
    const val DRAWER_WITH_USERNAME = "DrawerMenu/{${Args.USERNAME}}"
    const val HOME = "home"
    const val CATALOG = "catalog"
    const val MUESTRA_DATOS = "muestraDatos"
    const val DETAIL = "detail/{${Args.CODIGO}}"
    const val CART = "cart"
}

private object Args {
    const val USERNAME = "username"
    const val CODIGO = "codigo"
}
