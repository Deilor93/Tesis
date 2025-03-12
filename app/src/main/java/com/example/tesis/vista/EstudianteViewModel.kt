package com.example.tesis.vista

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesis.logica.DbClass
import com.example.tesis.logica.Estudiante
import kotlinx.coroutines.launch

class EstudianteViewModel(context: Context) : ViewModel() {
    private val db = DbClass.getDatabase(context)
    private val estudianteDao = db.estudianteDao()

    fun saveEstudiante(nombre: String, codigo: String) {
        viewModelScope.launch {
            val estudiante = Estudiante(nombre = nombre, codigo = codigo)
            estudianteDao.insertEstudiante(estudiante)
        }
    }
}
