package com.example.proyectologin005d.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit dedicado para JSONPlaceholder.
 *
 * Esto NO reemplaza tu ApiClient principal (10.0.2.2:8080),
 * es solo un cliente extra para consumir la API externa de ejemplo.
 */
object JsonPlaceholderApiClient {

    // Base URL oficial de JSONPlaceholder
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: JsonPlaceholderApiService by lazy {
        retrofit.create(JsonPlaceholderApiService::class.java)
    }
}
