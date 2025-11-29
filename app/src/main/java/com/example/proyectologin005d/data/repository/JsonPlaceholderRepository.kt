package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.source.remote.JsonPlaceholderApiClient
import com.example.proyectologin005d.data.source.remote.JsonPlaceholderPostDto

class JsonPlaceholderRepository {

    private val api = JsonPlaceholderApiClient.api

    // GET: lista de posts
    suspend fun getPosts(): Result<List<JsonPlaceholderPostDto>> {
        return try {
            val posts = api.getPosts()
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // POST de ejemplo: crea un post de prueba
    suspend fun createDemoPost(): Result<JsonPlaceholderPostDto> {
        return try {
            val nuevo = JsonPlaceholderPostDto(
                userId = 1,
                id = 0, // JSONPlaceholder ignora este ID y genera uno propio
                title = "Post de prueba desde MilSaboresMovil",
                body = "Este POST solo demuestra la comunicación con JSONPlaceholder."
            )
            val creado = api.createPost(nuevo)
            Result.success(creado)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // PUT de ejemplo: actualiza un post existente (por ejemplo, el id=1)
    suspend fun updateDemoPost(id: Int): Result<JsonPlaceholderPostDto> {
        return try {
            val actualizado = JsonPlaceholderPostDto(
                userId = 1,
                id = id,
                title = "Título actualizado (PUT de prueba)",
                body = "Este PUT demuestra cómo enviamos datos para modificar un recurso."
            )
            val resp = api.updatePost(id, actualizado)
            Result.success(resp)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // DELETE de ejemplo: elimina un post dado (por ejemplo, el id=1)
    suspend fun deleteDemoPost(id: Int): Result<Unit> {
        return try {
            api.deletePost(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
