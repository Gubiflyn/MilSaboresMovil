package com.example.proyectologin005d.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppFooter(navController: NavController) {
    val brown = Color(0xFF8B4513)
    val white = Color.White

    val backStackEntry by navController.currentBackStackEntryAsState()
    val current = backStackEntry?.destination?.route ?: ""

    NavigationBar(containerColor = brown) {

        NavigationBarItem(
            selected = current.startsWith("home"),
            onClick = { navController.navigate("home") { launchSingleTop = true } },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("HOME") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = white, selectedTextColor = white,
                unselectedIconColor = white.copy(alpha = .85f),
                unselectedTextColor = white.copy(alpha = .85f),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = current.startsWith("catalog"),
            onClick = { navController.navigate("catalog") { launchSingleTop = true } },
            icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Catálogo") },
            label = { Text("Catálogo") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = white, selectedTextColor = white,
                unselectedIconColor = white.copy(alpha = .85f),
                unselectedTextColor = white.copy(alpha = .85f),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = current.startsWith("history"),
            onClick = { navController.navigate("history") { launchSingleTop = true } },
            icon = { Icon(Icons.Filled.History, contentDescription = "Mi historial") },
            label = { Text("Mi historial") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = white, selectedTextColor = white,
                unselectedIconColor = white.copy(alpha = .85f),
                unselectedTextColor = white.copy(alpha = .85f),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = current.startsWith("profile"),
            onClick = { navController.navigate("profile") { launchSingleTop = true } },
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Mi Perfil") },
            label = { Text("Mi Perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = white, selectedTextColor = white,
                unselectedIconColor = white.copy(alpha = .85f),
                unselectedTextColor = white.copy(alpha = .85f),
                indicatorColor = Color.Transparent
            )
        )
    }
}

