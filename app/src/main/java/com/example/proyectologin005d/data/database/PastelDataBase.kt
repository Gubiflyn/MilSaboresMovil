package com.example.proyectologin005d.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel

// ⬇️ Agregados para historial
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import com.example.proyectologin005d.data.dao.OrderDao

@Database(
    entities = [
        Pastel::class,
        // ⬇️ Agregados para historial
        Order::class,
        OrderItem::class
    ],
    version = 3,
    exportSchema = false
)
abstract class PastelDatabase : RoomDatabase() {
    abstract fun pastelDao(): PastelDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: PastelDatabase? = null
        fun getInstance(context: Context): PastelDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PastelDatabase::class.java,
                    "pasteles.db"
                )
                    .fallbackToDestructiveMigration()   // ⬅️ fuerza recrear si hay cambios
                    .build().also { INSTANCE = it }
            }
    }
}
