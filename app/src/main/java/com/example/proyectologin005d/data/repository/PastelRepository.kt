// com/example/proyectologin005d/data/repository/PastelRepository.kt
package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.model.toEntity
import com.example.proyectologin005d.data.source.remote.PastelApiService
import kotlinx.coroutines.flow.Flow

class PastelRepository(
    private val dao: PastelDao,
    private val api: PastelApiService
) {

    /**
     * Sincroniza desde el backend. Si falla, usa el seed local SOLO si la tabla está vacía.
     */
    suspend fun syncFromRemote() {
        try {
            val remotos = api.getPasteles()
            val entidades: List<Pastel> = remotos.map { it.toEntity() }
            dao.insertAll(entidades)
        } catch (e: Exception) {
            if (dao.count() == 0) {
                // Fallback inicial
                dao.insertAll(LocalPastelData.seed)
            }
        }
    }

    // --- RESET 100% LOCAL (LO QUE QUIERES AHORA) ---

    /**
     * Borra todos los registros y vuelve a poblar SOLO con LocalPastelData.seed
     * (las 16 tortas definidas en LocalPastelData).
     */
    suspend fun resetFromLocalSeed() {
        dao.deleteAll()
        dao.insertAll(LocalPastelData.seed)
    }

    // --- Lecturas ---

    suspend fun getAllPasteles(): List<Pastel> = dao.getAll()

    fun observeAll(): Flow<List<Pastel>> = dao.observeAll()

    suspend fun getByCodigo(codigo: String): Pastel? = dao.getByCodigo(codigo)

    // --- CRUD ADMIN ---

    suspend fun crearPastel(pastel: Pastel) {
        dao.insert(pastel)
    }

    suspend fun actualizarPastel(pastel: Pastel) {
        dao.insert(pastel)
    }

    suspend fun eliminarPastel(codigo: String) {
        dao.deleteByCodigo(codigo)
    }
}
