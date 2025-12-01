package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.PastelDao
import com.example.proyectologin005d.data.model.PastelDto
import com.example.proyectologin005d.data.source.remote.PastelApiService
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class PastelRepositoryTest : StringSpec({

    "syncFromRemote inserta datos cuando la API devuelve lista válida" {
        val fakeDao = mockk<PastelDao>(relaxed = true)
        val fakeApi = mockk<PastelApiService>()

        coEvery { fakeApi.getPasteles() } returns listOf(
            PastelDto(
                codigo = "A1",
                categoria = "Tortas",
                nombre = "Selva Negra",
                precio = 14000
            )
        )

        val repo = PastelRepository(fakeDao, fakeApi)

        runTest {
            repo.syncFromRemote()
        }

        // Verificamos que se llame a insertAll en el DAO
        coVerify { fakeDao.insertAll(any()) }
    }
})
