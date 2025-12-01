package com.example.proyectologin005d.data.source.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Modelo simple para los posts de JSONPlaceholder
data class JsonPlaceholderPostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

interface JsonPlaceholderApiService {

    // GET: obtiene lista de posts
    @GET("posts")
    suspend fun getPosts(): List<JsonPlaceholderPostDto>

    // POST: crea un post de prueba
    @POST("posts")
    suspend fun createPost(
        @Body post: JsonPlaceholderPostDto
    ): JsonPlaceholderPostDto

    // PUT: actualiza un post de prueba
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: JsonPlaceholderPostDto
    ): JsonPlaceholderPostDto

    // DELETE: elimina un post de prueba
    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int
    )
}
