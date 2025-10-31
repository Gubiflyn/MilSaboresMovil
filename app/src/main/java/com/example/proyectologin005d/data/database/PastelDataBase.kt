package com.example.proyectologin005d.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.Pastel

@Database(entities = [Pastel::class], version = 1, exportSchema = false)
abstract class PastelDatabase : RoomDatabase() {
    abstract fun pastelDao(): PastelDao
}

