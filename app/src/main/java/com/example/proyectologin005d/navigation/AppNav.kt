package com.example.proyectologin005d.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectologin005d.login.LoginScreen
import com.example.proyectologin005d.ui.home.MuestraDatosScreen
import com.example.proyectologin005d.view.DrawerMenu
import com.example.proyectologin005d.register.RegisterScreen   // ✅ Importa la pantalla de registro (asegúrate del package)
import com.example.proyectologin005d.ui.home.HomeScreen


/*
 * AppNav: Define el grafo de navegación de la app.
 * - startDestination = "login"
 * - Rutas:
 *   - "login"
 *   - "register" (nueva)
 *   - "DrawerMenu/{username}"
 *   - "ProductoFormScreen/{nombre}/{precio}"
 */
@Composable
fun AppNav() {
    // creamos un controlador que gestione la navegacion
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // --- LOGIN ---
        composable("login") {
            LoginScreen(navController = navController)
        }

        // --- REGISTER (NUEVO) ---
        // Ruta simple sin argumentos para la pantalla de registro
        composable("register") {
            RegisterScreen(navController = navController)
        }

        // --- Drawer con parámetro username ---
        composable(
            route = "DrawerMenu/{username}",
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            DrawerMenu(username = username, navController = navController)
        }

        // --- ProductoFormScreen con 2 parámetros ---
        composable(
            route = "ProductoFormScreen/{nombre}/{precio}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("precio") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = Uri.encode(backStackEntry.arguments?.getString("nombre") ?: "")
            val precio = backStackEntry.arguments?.getString("precio") ?: ""

        }
        composable("home") {
            HomeScreen(navController = navController)
        }
    } // fin NavHost
} // fin AppNav
