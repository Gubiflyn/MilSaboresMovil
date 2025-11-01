package com.example.proyectologin005d.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
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
import com.example.proyectologin005d.ui.home.HomeViewModel
import com.example.proyectologin005d.ui.home.HomeViewModelFactory
import com.example.proyectologin005d.ui.home.MuestraDatosScreen
import com.example.proyectologin005d.ui.product.DetailScreen
import com.example.proyectologin005d.view.DrawerMenu
import com.example.proyectologin005d.ui.catalog.CatalogScreen


@Composable
fun AppNav() {
    val navController = rememberNavController()
    val ctx = LocalContext.current
    val homeVm: HomeViewModel = viewModel(factory = HomeViewModelFactory(ctx.applicationContext))

    NavHost(navController = navController, startDestination = "login") {

        // --- LOGIN ---
        composable("login") {
            LoginScreen(navController = navController)
        }

        // --- REGISTER ---
        composable("register") {
            RegisterScreen(navController = navController)
        }

        // --- DRAWER: SIN parámetro -> Invitado
        composable("DrawerMenu") {
            // DrawerMenu tiene username con valor por defecto = "Invitado"
            DrawerMenu(navController = navController)
        }

        // --- DRAWER: CON parámetro -> usuario real
        composable(
            route = "DrawerMenu/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            DrawerMenu(username = username, navController = navController)
        }

        // --- CATALOGO (usa el mismo ViewModel)
        composable("catalog") {
            CatalogScreen(navController = navController, viewModel = homeVm)
        }


        // --- ProductoFormScreen con 2 parámetros (placeholder)
        composable(
            route = "ProductoFormScreen/{nombre}/{precio}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("precio") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = Uri.encode(backStackEntry.arguments?.getString("nombre") ?: "")
            val precio = backStackEntry.arguments?.getString("precio") ?: ""
            // TODO: abrir tu pantalla de formulario con 'nombre' y 'precio'
        }

        // --- HOME (Catálogo con ViewModel)
        composable("home") {
            HomeScreen(navController = navController, viewModel = homeVm)
        }

        // --- DETALLE de producto
        composable(
            route = "detail/{codigo}",
            arguments = listOf(
                navArgument("codigo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val codigo = backStackEntry.arguments?.getString("codigo") ?: ""
            DetailScreen(navController = navController, codigo = codigo)
        }
    }
}



