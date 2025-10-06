package com.example.proyectologin006d.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectologin006d.login.LoginScreen
import com.example.proyectologin006d.ui.home.MuestraDatosScreen

@Composable


fun AppNav(){
    // crear un controlador que gestiones la navegacion
    val navController = rememberNavController()  // con memoria

    NavHost(navController=navController, startDestination = "login")
    {
        composable("login"){
            LoginScreen(navController=navController)
        }// Fin composable

    composable(
        route="muestraDatos/{username}",
        arguments = listOf(
            navArgument("username") {
                type = NavType.StringType
            } // fin argument
        )// fin listOf

    )// fin composable 2

    {   // inicio back
        backStackEntry ->
        val username = backStackEntry.arguments?.getString("username").orEmpty()
        MuestraDatosScreen(username=username, navController=navController)

    }// termino back

    }// fin NavHost
}// fin AppNav