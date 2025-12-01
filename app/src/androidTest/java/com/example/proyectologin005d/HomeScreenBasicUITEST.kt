package com.example.proyectologin005d.ui.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class HomeScreenBasicUITest {

    @get:Rule
    val composeTest = createComposeRule()

    @Test
    fun homeScreen_muestra_destacados() {
        composeTest.setContent {
            HomeScreenPreview()
        }

        composeTest.onNodeWithText("Destacados de hoy")
            .assertIsDisplayed()
    }
}

//