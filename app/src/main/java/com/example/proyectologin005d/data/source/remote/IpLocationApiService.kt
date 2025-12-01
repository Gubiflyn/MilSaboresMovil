package com.example.proyectologin005d.data.source.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET


data class IpLocationDto(
    val ip: String?,
    val city: String?,
    val region: String?,
    @SerializedName("country")
    val country_name: String?,
    val latitude: Double?,
    val longitude: Double?
)

interface IpLocationApiService {

    // Llama a https://ipwho.is/
    @GET("/")
    suspend fun getMyLocation(): IpLocationDto
}
