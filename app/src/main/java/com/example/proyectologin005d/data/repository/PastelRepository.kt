package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel
import kotlinx.coroutines.flow.Flow

class PastelRepository(private val dao: PastelDao) {

    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.insertAll(LocalPastelData.seed)
        }
    }

    suspend fun getAllPasteles(): List<Pastel> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<Pastel>> {
        return dao.observeAll()
    }

    suspend fun getByCodigo(codigo: String): Pastel? {
        return dao.getByCodigo(codigo)
    }
}
