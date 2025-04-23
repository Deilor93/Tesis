package com.example.tesis.vista

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _screenStack = MutableStateFlow(listOf("game")) // Pila para registrar historial de navegacion
    val currentScreen = _screenStack.asStateFlow()

    //Avanzar a siguiente pantalla
    fun navigateTo(screen: String) {
        _screenStack.value = _screenStack.value + screen // Agrega la pantalla a la pila
    }

    //Regresar a pantalla anterior
    fun goBack() {
        if (_screenStack.value.size > 1) {
            _screenStack.value = _screenStack.value.dropLast(1) // Quita ultima pantalla de la pila
        }
    }
}
