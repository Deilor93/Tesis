package com.example.tesis.vista

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _screenStack = MutableStateFlow(listOf("game")) // Stack to track navigation history
    val currentScreen = _screenStack.asStateFlow()

    fun navigateTo(screen: String) {
        _screenStack.value = _screenStack.value + screen // Push screen onto the stack
    }

    fun goBack() {
        if (_screenStack.value.size > 1) {
            _screenStack.value = _screenStack.value.dropLast(1) // Pop last screen
        }
    }
}
