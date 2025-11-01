package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel

class PastelRepository(private val dao: PastelDao) {

    suspend fun seedIfEmpty() {
        if (dao.getAll().isEmpty()) {                 // <-- getAll()
            dao.insertAll(LocalPastelData.seed)       // <-- nombre real de tu lista
        }
    }

    suspend fun getAllPasteles(): List<Pastel> = dao.getAll()

    suspend fun getByCodigo(codigo: String): Pastel? = dao.getByCodigo(codigo)
}


