package com.example.tesis.vista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainScreen : ComponentActivity() {
    private val navigationViewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val screenStack by navigationViewModel.currentScreen.collectAsState()
            val currentScreen = screenStack.last() // Get top screen in the stack

            when (currentScreen) {
                "game" -> IntroScreen(navigationViewModel, this)
                "welcome" -> WelcomeScreen(navigationViewModel)
            }
        }
    }
}
