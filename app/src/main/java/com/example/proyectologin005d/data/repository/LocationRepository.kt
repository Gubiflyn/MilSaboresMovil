package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.source.remote.IpLocationApiClient
import com.example.proyectologin005d.data.source.remote.IpLocationDto

class LocationRepository {

    private val api = IpLocationApiClient.api

    suspend fun getMyLocation(): Result<IpLocationDto> {
        return try {
            val location = api.getMyLocation()
            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
