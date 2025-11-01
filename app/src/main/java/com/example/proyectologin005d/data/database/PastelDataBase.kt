package com.example.proyectologin005d.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel

@Database(entities = [Pastel::class], version = 2, exportSchema = false) // ↑ sube versión
abstract class PastelDatabase : RoomDatabase() {
    abstract fun pastelDao(): PastelDao

    companion object {
        @Volatile private var INSTANCE: PastelDatabase? = null
        fun getInstance(context: Context): PastelDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PastelDatabase::class.java,
                    "pasteles.db"
                )
                    .fallbackToDestructiveMigration()  // ← recrea con la nueva version
                    .build().also { INSTANCE = it }
            }
    }
}




