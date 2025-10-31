package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.source.LocalPastelData

class PastelRepository {

    fun getAllPasteles(): List<Pastel> {
        return LocalPastelData.pastelesList
    }

    fun getByCategoria(categoria: String): List<Pastel> {
        return LocalPastelData.pastelesList.filter { it.categoria == categoria }
    }

    fun getByCodigo(codigo: String): Pastel? {
        return LocalPastelData.pastelesList.find { it.codigo == codigo }
    }
}
