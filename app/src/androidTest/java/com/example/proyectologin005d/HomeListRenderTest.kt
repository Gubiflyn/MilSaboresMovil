package com.example.proyectologin005d.ui.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.graphics.Color

class HomeListRenderTest {

    @get:Rule
    val composeTest = createComposeRule()

    @Test
    fun featuredCard_renderiza_titulo_y_descripcion() {

        composeTest.setContent {
            FeaturedCard(
                title = "Torta de Prueba",
                desc = "Descripción de prueba",
                bg = Color.White,
                fg = Color.Black,
                onClick = {}
            )
        }

        composeTest.onNodeWithText("Torta de Prueba").assertIsDisplayed()
        composeTest.onNodeWithText("Descripción de prueba").assertIsDisplayed()
    }
}
