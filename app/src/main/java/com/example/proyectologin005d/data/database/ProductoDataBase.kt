package com.example.proyectologin005d.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectologin005d.data.dao.ProductoDao
import com.example.proyectologin005d.data.model.Producto

@Database(
    entities = [Producto::class],
    version=1,
    exportSchema = false  // evito evita warning
)

abstract class ProductoDatabase: RoomDatabase(){
    abstract fun productoDao(): ProductoDao

    companion object{
        @Volatile
        // Cualquier cambio en el valor de la Instancie sea visible en el hilo
        private var INSTANCE:ProductoDatabase? =null

        fun getDatabase(context: Context): ProductoDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"

                ).build() // fin databaseBuilder
                INSTANCE = instance
                instance

            }// fin return

        }// fin getDatabase


    }// fin  companion object

}//  fin class ProductoDataBase: