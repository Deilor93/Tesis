package com.example.tesis.vista.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.tesis.R
import com.example.tesis.logica.Estudiante
import com.example.tesis.logica.Resultado
import com.example.tesis.vista.modelo.DraggableItem

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

    val amigosSoly: SnapshotStateList<DraggableItem> = mutableStateListOf(
        DraggableItem("A", R.drawable.amigo_1),
        DraggableItem("B", R.drawable.amigo_2),
        DraggableItem("C", R.drawable.amigo_3),
        DraggableItem("D", R.drawable.amigo_4),
        DraggableItem("E", R.drawable.amigo_5),
        DraggableItem("F", R.drawable.amigo_6),
    )

    val assignedStatus: SnapshotStateList<Boolean> = mutableStateListOf<Boolean>().apply {
        repeat(6) { add(false) }
    }

}