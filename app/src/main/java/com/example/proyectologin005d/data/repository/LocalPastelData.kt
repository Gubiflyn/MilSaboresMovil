package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Pastel

object LocalPastelData {

    val seed = listOf(
        Pastel(
            codigo = "TC001",
            categoria = "Tortas Cuadradas",
            nombre = "Torta Cuadrada de Chocolate",
            precio = 45000,
            imagen = "tor_cuad_cho",
            descripcion = "Bizcocho de chocolate con crema y ganache.",
            stock = 10
        ),
        Pastel(
            codigo = "TC002",
            categoria = "Tortas Cuadradas",
            nombre = "Torta Cuadrada de Frutas",
            precio = 50000,
            imagen = "tor_frutas",
            descripcion = "Bizcocho vainilla con frutas naturales.",
            stock = 10
        ),
        Pastel(
            codigo = "TT001",
            categoria = "Tortas Circulares",
            nombre = "Torta Circular de Vainilla",
            precio = 40000,
            imagen = "tor_vainilla",
            descripcion = "Bizcocho vainilla con crema pastelera.",
            stock = 10
        ),
        Pastel(
            codigo = "TT002",
            categoria = "Tortas Circulares",
            nombre = "Torta Circular de Manjar",
            precio = 42000,
            imagen = "tor_cir_manj",
            descripcion = "Bizcocho con manjar y merengue.",
            stock = 10
        ),
        Pastel(
            codigo = "PI001",
            categoria = "Postres Individuales",
            nombre = "Mousse de Chocolate",
            precio = 5000,
            imagen = "mouss_choc",
            descripcion = "Postre individual cremoso de cacao.",
            stock = 50
        ),
        Pastel(
            codigo = "PI002",
            categoria = "Postres Individuales",
            nombre = "Tiramisú Clásico",
            precio = 5500,
            imagen = "tir_clas",
            descripcion = "Clásico italiano con café y mascarpone.",
            stock = 50
        ),
        Pastel(
            codigo = "PSA001",
            categoria = "Productos Sin Azúcar",
            nombre = "Torta Sin Azúcar de Naranja",
            precio = 48000,
            imagen = "torta_nara",
            descripcion = "Endulzada con stevia, apta diabéticos.",
            stock = 8
        ),
        Pastel(
            codigo = "PSA002",
            categoria = "Productos Sin Azúcar",
            nombre = "Cheesecake Sin Azúcar",
            precio = 47000,
            imagen = "chee_sin_azu",
            descripcion = "Cheesecake bajo en azúcares añadidos.",
            stock = 8
        ),
        Pastel(
            codigo = "PT001",
            categoria = "Pastelería Tradicional",
            nombre = "Empanada de Manzana",
            precio = 3000,
            imagen = "emp_manza",
            descripcion = "Rellena de manzana y canela.",
            stock = 40
        ),
        Pastel(
            codigo = "PT002",
            categoria = "Pastelería Tradicional",
            nombre = "Tarta de Santiago",
            precio = 6000,
            imagen = "tarta_sant",
            descripcion = "Clásica tarta de almendra.",
            stock = 25
        ),
        Pastel(
            codigo = "PG001",
            categoria = "Productos sin gluten",
            nombre = "brownie_gl",
            precio = 4000,
            imagen = "brownie_gl",
            descripcion = "Brownie de cacao sin gluten.",
            stock = 30
        ),
        Pastel(
            codigo = "PG002",
            categoria = "Productos sin gluten",
            nombre = "Pan Sin Gluten",
            precio = 3500,
            imagen = "pan_sin_glu",
            descripcion = "Pan artesanal sin gluten.",
            stock = 30
        ),
        Pastel(
            codigo = "PV001",
            categoria = "Productos Vegana",
            nombre = "Torta Vegana de Chocolate",
            precio = 50000,
            imagen = "tor_veg_cho",
            descripcion = "100% vegetal, sin productos animales.",
            stock = 6
        ),
        Pastel(
            codigo = "PV002",
            categoria = "Productos Vegana",
            nombre = "Galletas Veganas de Avena",
            precio = 4500,
            imagen = "gall_vega",
            descripcion = "Avena y frutos secos.",
            stock = 60
        ),
        Pastel(
            codigo = "TE001",
            categoria = "Tortas Especiales",
            nombre = "Torta Especial de Cumpleaños",
            precio = 55000,
            imagen = "tor_cump",
            descripcion = "Decoración personalizada.",
            stock = 5
        ),
        Pastel(
            codigo = "TE002",
            categoria = "Tortas Especiales",
            nombre = "Torta Especial de Boda",
            precio = 60000,
            imagen = "tor_boda",
            descripcion = "Diseño elegante para bodas.",
            stock = 3
        )
    )
}
