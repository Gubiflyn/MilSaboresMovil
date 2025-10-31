package com.example.proyectologin005d.data.source

import com.example.proyectologin005d.data.model.Pastel

object LocalPastelData {
    val pastelesList = listOf(
        Pastel(
            codigo = "TC001",
            categoria = "Tortas Cuadradas",
            nombre = "Torta Cuadrada de Chocolate",
            precio = 45000,
            imagen = "img/TCChocolate.webp",
            descripcion = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas.",
            stock = 12
        ),
        Pastel(
            codigo = "TC002",
            categoria = "Tortas Cuadradas",
            nombre = "Torta Cuadrada de Frutas",
            precio = 50000,
            imagen = "img/TortaCuadradaDeFrutas.jpg",
            descripcion = "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla.",
            stock = 8
        )
        // 🔹 Aquí agregas los demás pasteles desde tu JSON
    )
}
