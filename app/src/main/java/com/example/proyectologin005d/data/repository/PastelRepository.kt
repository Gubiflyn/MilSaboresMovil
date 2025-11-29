// com/example/proyectologin005d/data/repository/PastelRepository.kt
package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.model.toEntity
import com.example.proyectologin005d.data.source.LocalPastelData
import com.example.proyectologin005d.data.source.remote.PastelApiService
import kotlinx.coroutines.flow.Flow

class PastelRepository(
    private val dao: PastelDao,
    private val api: PastelApiService
) {

    /**
     * Sincroniza los pasteles desde el microservicio REST hacia la base local (Room).
     * Si falla la API, usa los datos locales de ejemplo como respaldo.
     */
    suspend fun syncFromRemote() {
        try {
            val remotos = api.getPasteles()
            val entidades: List<Pastel> = remotos.map { it.toEntity() }
            dao.insertAll(entidades)
        } catch (e: Exception) {
            // Fallback: si falla el backend, sembramos datos locales si la tabla está vacía
            if (dao.count() == 0) {
                dao.insertAll(LocalPastelData.seed)
            }
        }
    }

    // --- Lecturas existentes ---

    suspend fun getAllPasteles(): List<Pastel> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<Pastel>> {
        return dao.observeAll()
    }

    suspend fun getByCodigo(codigo: String): Pastel? {
        return dao.getByCodigo(codigo)
    }

    // --- CRUD para usar desde el rol ADMIN ---

    /**
     * Crea un nuevo pastel en el catálogo.
     * Si ya existe un pastel con el mismo código, será reemplazado.
     */
    suspend fun crearPastel(pastel: Pastel) {
        dao.insert(pastel)
    }

    /**
     * Actualiza un pastel existente (mismo comportamiento que crear, por REPLACE).
     */
    suspend fun actualizarPastel(pastel: Pastel) {
        dao.insert(pastel)
    }

    /**
     * Elimina un pastel por su código.
     */
    suspend fun eliminarPastel(codigo: String) {
        dao.deleteByCodigo(codigo)
    }
}
