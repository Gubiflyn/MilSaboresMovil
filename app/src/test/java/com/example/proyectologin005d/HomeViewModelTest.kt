package com.example.proyectologin005d.ui.home

import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.repository.PastelRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : StringSpec({

    "HomeViewModel actualiza los items desde repository.observeAll()" {
        val fakePasteles = listOf(
            Pastel("C001", "Tortas", "Mil Hojas", 12000),
            Pastel("C002", "Kuchen", "Frambuesa", 8500)
        )

        val repo = mockk<PastelRepository>()
        coEvery { repo.observeAll() } returns flowOf(fakePasteles)
        coEvery { repo.syncFromRemote() } returns Unit

        val vm = HomeViewModel(repo)

        vm.ui.value.items.size shouldBe 2
        vm.ui.value.items[0].nombre shouldBe "Mil Hojas"
    }
})
//