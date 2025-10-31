package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectologin005d.data.model.Pastel

@Dao
interface PastelDao {
    @Query("SELECT * FROM pasteles")
    suspend fun getAll(): List<Pastel>

    @Query("SELECT * FROM pasteles WHERE codigo = :codigo LIMIT 1")
    suspend fun getByCodigo(codigo: String): Pastel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Pastel>)

    @Query("DELETE FROM pasteles")
    suspend fun clear()
}
