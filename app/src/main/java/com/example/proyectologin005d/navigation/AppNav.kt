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
import com.example.proyectologin005d.view.ProductoFormScreen

@Composable

fun AppNav(){
    // creamos un controilador que gestione la navegacion
    val navController = rememberNavController()

    NavHost( navController=navController, startDestination = "login"){
        composable("login"){
            LoginScreen( navController=navController)
        }// composable

        composable(
            route="DrawerMenu/{username}",
             arguments = listOf(
                 navArgument("username"){
                     type= NavType.StringType
                 }
             )   //fin listoF

        ) // fin composable 2

        {// inicio back
            backStackEntry ->
            val username=backStackEntry.arguments?.getString("username").orEmpty()
            DrawerMenu(username=username,navController=navController  )

        }// termino back

// routa para ProductoFormScreen

        composable(
            route="ProductoFormScreen/{nombre}/{precio}",
            arguments = listOf(
                navArgument("nombre"){ type= NavType.StringType },
                navArgument("precio"){ type= NavType.StringType }
            )   //fin listoF
        )// fin composable 3

        {// inicio back
            backStackEntry ->
            val nombre= Uri.encode(backStackEntry.arguments?.getString("nombre") ?:"")
            val precio= backStackEntry.arguments?.getString("precio") ?:""

            ProductoFormScreen(nombre=nombre,precio=precio, navController=navController  )

        }// termino back 3





    }//fin NavHost

}// fin AppNav