package com.example.proyectologin005d.data.source.remote

import com.example.proyectologin005d.data.model.PastelDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Interfaz Retrofit para hablar con la API REST de pasteles
interface PastelApiService {

    // GET http://10.0.2.2:8080/api/pasteles
    @GET("api/pasteles")
    suspend fun getPasteles(): List<PastelDto>

    // POST http://10.0.2.2:8080/api/pasteles
    // Crea un nuevo pastel
    @POST("api/pasteles")
    suspend fun crearPastel(
        @Body pastel: PastelDto
    ): PastelDto

    // PUT http://10.0.2.2:8080/api/pasteles/{codigo}
    // Actualiza un pastel existente
    @PUT("api/pasteles/{codigo}")
    suspend fun actualizarPastel(
        @Path("codigo") codigo: String,
        @Body pastel: PastelDto
    ): PastelDto

    // DELETE http://10.0.2.2:8080/api/pasteles/{codigo}
    // Elimina un pastel
    @DELETE("api/pasteles/{codigo}")
    suspend fun eliminarPastel(
        @Path("codigo") codigo: String
    )
}
