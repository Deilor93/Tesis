package com.example.tesis.vista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tesis.logica.Estudiante

class SharedViewModel : ViewModel() {
    var estudiante: Estudiante? by mutableStateOf(null)

}
