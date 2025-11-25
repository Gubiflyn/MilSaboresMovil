package com.example.proyectologin005d.data.source.remote

import com.example.proyectologin005d.data.model.PastelDto
import retrofit2.http.GET

interface PastelApiService {

    // Esto llama a: GET http://BASE_URL/api/pasteles
    @GET("api/pasteles")
    suspend fun getPasteles(): List<PastelDto>
}
