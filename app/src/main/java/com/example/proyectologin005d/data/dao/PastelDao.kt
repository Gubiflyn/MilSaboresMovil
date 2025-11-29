package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectologin005d.data.model.Pastel
import kotlinx.coroutines.flow.Flow

@Dao
interface PastelDao {

    @Query("SELECT * FROM pasteles")
    suspend fun getAll(): List<Pastel>

    @Query("SELECT * FROM pasteles")
    fun observeAll(): Flow<List<Pastel>>

    @Query("SELECT COUNT(*) FROM pasteles")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Pastel>)

    @Query("SELECT * FROM pasteles WHERE codigo = :codigo LIMIT 1")
    suspend fun getByCodigo(codigo: String): Pastel?

    // --- CRUD admin ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pastel: Pastel)

    @Query("DELETE FROM pasteles WHERE codigo = :codigo")
    suspend fun deleteByCodigo(codigo: String)

    // --- Reset completo del catálogo ---
    @Query("DELETE FROM pasteles")
    suspend fun deleteAll()
}
