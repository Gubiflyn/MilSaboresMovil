package com.example.proyectologin005d.ui.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class HomeListRenderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun featuredCard_muestraTituloYDescripcion() {
        composeTestRule.setContent {
            FeaturedCard(
                title = "Torta de Prueba",
                desc = "Descripción de prueba",
                bg = Color.White,
                fg = Color.Black,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Torta de Prueba").assertIsDisplayed()
        composeTestRule.onNodeWithText("Descripción de prueba").assertIsDisplayed()
    }
}

//