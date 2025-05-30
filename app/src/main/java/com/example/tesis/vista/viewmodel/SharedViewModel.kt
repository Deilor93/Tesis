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

    var flagCor by mutableStateOf(false)

    var potLing: Int = 0
    var potMus: Int = 0
    var potIntra: Int = 0
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

    val bichos: SnapshotStateList<DraggableItem> = mutableStateListOf(
        DraggableItem("A", R.drawable.mosca, rotation = 0f),
        DraggableItem("B", R.drawable.mosca, rotation = 180f),
        DraggableItem("C", R.drawable.mosca, rotation = 90f),
        DraggableItem("D", R.drawable.mosca, rotation = 270f),
        DraggableItem("E", R.drawable.mosquito, flipH = true),
        DraggableItem("F", R.drawable.mosquito, rotation = 270f),
        DraggableItem("G", R.drawable.mosquito, rotation = 90f),
        DraggableItem("H", R.drawable.mosquito, rotation = 0f),
        DraggableItem("I", R.drawable.catarina, rotation = 0f),
        DraggableItem("J", R.drawable.catarina, rotation = 270f),
        DraggableItem("K", R.drawable.catarina, rotation = 90f),
        DraggableItem("L", R.drawable.catarina, flipH = true),
        DraggableItem("M", R.drawable.mariposa, rotation = 270f),
        DraggableItem("N", R.drawable.mariposa, rotation = 90f),
        DraggableItem("O", R.drawable.mariposa, rotation = 180f),
        DraggableItem("P", R.drawable.mariposa, rotation = 0f)
    )

    private var hasShuffledPuzzle = false

    val puzzle: SnapshotStateList<DraggableItem> = mutableStateListOf(
        DraggableItem("A", R.drawable.puzzle_1),
        DraggableItem("B", R.drawable.puzzle_2),
        DraggableItem("C", R.drawable.puzzle_3),
        DraggableItem("D", R.drawable.puzzle_4),
        DraggableItem("E", R.drawable.puzzle_5),
        DraggableItem("F", R.drawable.puzzle_6),
        DraggableItem("G", R.drawable.puzzle_7),
        DraggableItem("H", R.drawable.puzzle_8),
        DraggableItem("I", R.drawable.puzzle_9),
        DraggableItem("J", R.drawable.puzzle_10),
        DraggableItem("K", R.drawable.puzzle_11),
        DraggableItem("L", R.drawable.puzzle_12),
        DraggableItem("M", R.drawable.puzzle_13),
        DraggableItem("N", R.drawable.puzzle_14),
        DraggableItem("O", R.drawable.puzzle_15),
        DraggableItem("P", R.drawable.puzzle_16)
    )

    init {
        shufflePuzzleOnce()
    }

    private fun shufflePuzzleOnce() {
        if (!hasShuffledPuzzle) {
            val shuffled = puzzle.shuffled()
            puzzle.clear()
            puzzle.addAll(shuffled)
            hasShuffledPuzzle = true
        }
    }

    val p23: List<String> = listOf(
        "Utiliza tu imaginacion!"
    )

    val figurasP23 = mutableStateListOf(
        DraggableItem("A", R.drawable.circulo, correctLabel = "Circulo"),
        DraggableItem("B", R.drawable.cuadrado, correctLabel = "Cuadrado"),
        DraggableItem("C", R.drawable.estrella, correctLabel = "Estrella"),
        DraggableItem("D", R.drawable.hexagono, correctLabel = "Hexagono"),
        DraggableItem("E", R.drawable.octagono, correctLabel = "Octagono"),
        DraggableItem("F", R.drawable.ovalo, correctLabel = "Ovalo"),
        DraggableItem("G", R.drawable.paralelogramo, correctLabel = "Paralelogramo"),
        DraggableItem("H", R.drawable.pentagono, correctLabel = "Pentagono"),
        DraggableItem("I", R.drawable.rectangulo, correctLabel = "Rectangulo"),
        DraggableItem("J", R.drawable.rombo, correctLabel = "Rombo"),
        DraggableItem("K", R.drawable.trapecio, correctLabel = "Trapecio"),
        DraggableItem("L", R.drawable.triangulo, correctLabel = "Triangulo")
    )

    val p39: List<String> = listOf(
        "Animales de granja",
        "Dinosaurios"
    )

    val figurasP39 = mutableStateListOf(
        DraggableItem("A", R.drawable.caballo),
        DraggableItem("B", R.drawable.dino_1),
        DraggableItem("C", R.drawable.dino_2),
        DraggableItem("D", R.drawable.estego),
        DraggableItem("E", R.drawable.gallina),
        DraggableItem("F", R.drawable.oveja),
        DraggableItem("G", R.drawable.pato),
        DraggableItem("H", R.drawable.trex),
        DraggableItem("I", R.drawable.vaca),
        DraggableItem("J", R.drawable.velocirraptor)
    )

    val p40: List<String> = listOf(
        "Zebra",
        "Pajaro",
        "Pez",
        "Caracol",
        "Gorila"
    )

    val animalesSombra = mutableStateListOf(
        DraggableItem("A", R.drawable.gorila, correctLabel = "Gorila"),
        DraggableItem("B", R.drawable.pez, correctLabel = "Pez"),
        DraggableItem("C", R.drawable.caracol, correctLabel = "Caracol"),
        DraggableItem("D", R.drawable.zebra, correctLabel = "Zebra"),
        DraggableItem("E", R.drawable.pajaro, correctLabel = "Pajaro")
    )

    val sombras = mutableStateListOf(
        DraggableItem("A", R.drawable.sombra_zebra, correctLabel = "Zebra"),
        DraggableItem("B", R.drawable.sombra_pajaro, correctLabel = "Pajaro"),
        DraggableItem("C", R.drawable.sombra_pez, correctLabel = "Pez"),
        DraggableItem("D", R.drawable.sombra_caracol, correctLabel = "Caracol"),
        DraggableItem("E", R.drawable.sombra_gorila, correctLabel = "Gorila")
    )

    val p41: List<String> = listOf(
        "Frutas",
        "Utiles Escolares",
        "Animales"
    )

    val figurasP41 = mutableStateListOf(
        DraggableItem("A", R.drawable.borrador),
        DraggableItem("B", R.drawable.lapiz),
        DraggableItem("C", R.drawable.leon),
        DraggableItem("D", R.drawable.manzana),
        DraggableItem("E", R.drawable.pera),
        DraggableItem("F", R.drawable.perro),
        DraggableItem("G", R.drawable.pollito),
        DraggableItem("H", R.drawable.tijeras),
        DraggableItem("I", R.drawable.uvas)
    )

    val assignedStatus: SnapshotStateList<Boolean> = mutableStateListOf<Boolean>().apply {
        repeat(6) { add(false) }
    }

}