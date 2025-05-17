package com.example.tesis.vista.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tesis.logica.Estudiante
import com.example.tesis.logica.Resultado

//ViewModel compartido para almacenar variables utilizadas en mas de una pantalla
class SharedViewModel : ViewModel() {
    var estudiante: Estudiante? by mutableStateOf(null)

    var resultado: Resultado? by mutableStateOf(null)

    var contLing: Int = 0
    var contMat: Int = 0
    var contEsp: Int = 0
    var contMus: Int = 0
    var contCor: Int = 0
    var contInter: Int = 0
    var contIntra: Int = 0
    var contNatu: Int = 0

}