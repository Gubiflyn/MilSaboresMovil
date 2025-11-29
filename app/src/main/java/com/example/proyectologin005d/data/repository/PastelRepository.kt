package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.model.PastelDto
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
                // Fallback inicial: si no hay backend y la tabla está vacía,
                // usamos las tortas locales.
                dao.insertAll(LocalPastelData.seed)
            }
        }
    }

    // --- RESET 100% LOCAL (LO QUE YA USABAS) ---

    /**
     * Borra todos los registros y vuelve a poblar SOLO con LocalPastelData.seed
     * (las 16 tortas definidas en LocalPastelData).
     */
    suspend fun resetFromLocalSeed() {
        dao.deleteAll()
        dao.insertAll(LocalPastelData.seed)
    }

    // --- LECTURA GENERAL ---

    fun observeAll(): Flow<List<Pastel>> = dao.observeAll()

    suspend fun getByCodigo(codigo: String): Pastel? = dao.getByCodigo(codigo)

    // --- Helpers de mapeo Pastel <-> PastelDto ---

    private fun Pastel.toDto(): PastelDto =
        PastelDto(
            codigo = codigo,
            categoria = categoria,
            nombre = nombre,
            precio = precio,
            imagen = imagen,
            descripcion = descripcion,
            stock = stock
        )

    // --- CRUD ADMIN ---

    /**
     * Crea un pastel nuevo desde el panel de admin.
     * 1) Intenta crearlo en la API (POST)
     * 2) Si la API responde OK, guarda lo que devuelve en Room
     * 3) Si la API falla (no hay backend), lo guarda solo en Room (comportamiento antiguo)
     */
    suspend fun crearPastel(pastel: Pastel) {
        try {
            val creadoRemoto = api.crearPastel(pastel.toDto())
            dao.insert(creadoRemoto.toEntity())
        } catch (e: Exception) {
            // Fallback: sin backend, mantenemos el comportamiento local
            dao.insert(pastel)
        }
    }

    /**
     * Actualiza un pastel desde el panel de admin.
     * Mismo enfoque: primero API (PUT), luego Room; si falla, solo Room.
     */
    suspend fun actualizarPastel(pastel: Pastel) {
        try {
            val actualizadoRemoto = api.actualizarPastel(
                codigo = pastel.codigo,
                pastel = pastel.toDto()
            )
            dao.insert(actualizadoRemoto.toEntity())
        } catch (e: Exception) {
            // Fallback local
            dao.insert(pastel)
        }
    }

    /**
     * Elimina un pastel desde el panel admin.
     * Intenta DELETE en la API; si falla, de todos modos borra en Room
     * para que la app siga funcionando como antes.
     */
    suspend fun eliminarPastel(codigo: String) {
        try {
            api.eliminarPastel(codigo)
        } catch (e: Exception) {
            // Si no hay backend, ignoramos el error y seguimos
        } finally {
            // Siempre borramos de la BD local para mantener el catálogo
            // consistente en la app.
            dao.deleteByCodigo(codigo)
        }
    }
}
