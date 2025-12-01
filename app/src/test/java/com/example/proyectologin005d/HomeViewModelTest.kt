package com.example.proyectologin005d.ui.home

import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.repository.PastelRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : StringSpec({

    // Dispatcher de prueba para reemplazar Dispatchers.Main en los tests
    val testDispatcher = StandardTestDispatcher()

    // Antes de cada test: “engañamos” a Dispatchers.Main
    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    // Después de cada test: dejamos todo como estaba
    afterTest {
        Dispatchers.resetMain()
    }

    "HomeViewModel actualiza los items desde repository.observeAll()" {
        val fakePasteles = listOf(
            Pastel(codigo = "C001", categoria = "Tortas", nombre = "Mil Hojas", precio = 12000),
            Pastel(codigo = "C002", categoria = "Kuchen", nombre = "Frambuesa", precio = 8500)
        )

        val repo = mockk<PastelRepository>()
        coEvery { repo.observeAll() } returns flowOf(fakePasteles)
        coEvery { repo.syncFromRemote() } returns Unit

        val vm = HomeViewModel(repository = repo)

        // Dejamos que se ejecuten las coroutines del ViewModel
        testDispatcher.scheduler.advanceUntilIdle()

        vm.ui.value.items.size shouldBe 2
        vm.ui.value.items[0].nombre shouldBe "Mil Hojas"
    }
})//
