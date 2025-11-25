package com.example.proyectologin005d.data.source

import com.example.proyectologin005d.data.model.Pastel

object LocalPastelData {

    // Lista de datos de ejemplo para sembrar la BD cuando la API falle
    val seed: List<Pastel> = listOf(
        Pastel(
            codigo = "P001",
            nombre = "Torta Tres Leches",
            descripcion = "Bizcocho húmedo con mezcla de tres leches",
            precio = 12990,
            categoria = "Tortas"
        ),
        Pastel(
            codigo = "P002",
            nombre = "Cheesecake Frutilla",
            descripcion = "Base de galleta con relleno de queso crema y frutillas",
            precio = 13990,
            categoria = "Postres"
        ),
        Pastel(
            codigo = "P003",
            nombre = "Brazo de Reina",
            descripcion = "Rollo de bizcocho relleno de manjar",
            precio = 8990,
            categoria = "Pastelería clásica"
        )
        // agrega más si quieres
    )
}
