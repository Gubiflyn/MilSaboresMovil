package com.example.proyectologin005d.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectologin005d.login.LoginScreen
import com.example.proyectologin005d.ui.home.MuestraDatosScreen

@Composable

fun AppNav(){
    // creamos un controilador que gestione la navegacion
    val navController = rememberNavController()

    NavHost( navController=navController, startDestination = "login"){
        composable("login"){
            LoginScreen( navController=navController)
        }// composable

        composable(
            route="muestraDatos/{username}",
             arguments = listOf(
                 navArgument("username"){
                     type= NavType.StringType
                 }
             )   //fin listoF

        ) // fin composable 2

        {// inicio back
            backStackEntry ->
            val username=backStackEntry.arguments?.getString("username").orEmpty()
            MuestraDatosScreen(username=username,navController=navController  )

        }// termino back


    }//fin NavHost



}// fin AppNav