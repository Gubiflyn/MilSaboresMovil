package com.example.proyectologin005d.data.model

// Modelo que llega desde la API REST (JSON)
data class PastelDto(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Int,
    val imagen: String? = null,
    val descripcion: String? = null,
    val stock: Int = 0
)

// Mapeo de DTO → Entidad Room
fun PastelDto.toEntity(): Pastel = Pastel(
    codigo = codigo,
    categoria = categoria,
    nombre = nombre,
    precio = precio,
    imagen = imagen,
    descripcion = descripcion,
    stock = stock
)
