package com.example.tesis.vista.componente

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.example.tesis.R
import com.example.tesis.vista.util.MostrarGif
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AnimacionImagenPorNivel(
    modifier: Modifier = Modifier,
    currentStep: Int
) {
    val imageStep = currentStep / 5

    var hasStartedOnce by remember { mutableStateOf(false) }

    var showGif by remember { mutableStateOf(false) }

    // Indice de la imagen actualmente mostrada
    var displayedImageIndex by remember { mutableIntStateOf(0) }
    //Selecciona aleatoriamente una de las tres posibles lineas de evolucion
    val imgSelector = Random.nextInt(3)

    // Mantiene el registro de cuando se debe actualizar la imagen
    LaunchedEffect(imageStep) {
        //Muestra el GIF 800 milisegundos cuando se hace el cambio de imagen
        if (hasStartedOnce && imageStep != displayedImageIndex) {
            showGif = true
            delay(800)
            displayedImageIndex = imageStep
            showGif = false
        //Mantiene la imagen mostrada en pantalla cuando no se muestra el GIF
        } else {
            hasStartedOnce = true
            displayedImageIndex = imageStep
        }
    }

    val solyPirata = listOf(
        R.drawable.monstruo,
        R.drawable.monstruo_1,
        R.drawable.monstruo_1_1,
        R.drawable.monstruo_1_2,
        R.drawable.monstruo_1_3,
        R.drawable.monstruo_1_4,
        R.drawable.monstruo_1_5,
        R.drawable.monstruo_1_6,
        R.drawable.monstruo_1_7
    )

    val solyCaballero = listOf(
        R.drawable.monstruo,
        R.drawable.monstruo_2,
        R.drawable.monstruo_2_1,
        R.drawable.monstruo_2_2,
        R.drawable.monstruo_2_3,
        R.drawable.monstruo_2_4,
        R.drawable.monstruo_2_5,
        R.drawable.monstruo_2_6,
        R.drawable.monstruo_2_7
    )

    val solyPrincipe = listOf(
        R.drawable.monstruo,
        R.drawable.monstruo_3,
        R.drawable.monstruo_3_1,
        R.drawable.monstruo_3_2,
        R.drawable.monstruo_3_3,
        R.drawable.monstruo_3_4,
        R.drawable.monstruo_3_5,
        R.drawable.monstruo_3_6,
        R.drawable.monstruo_3_7

    )
    val solyImages = remember {
        when (imgSelector) {
            0 -> solyPirata
            1 -> solyCaballero
            2 -> solyPrincipe
            else -> listOf(R.drawable.monstruo)
        }
    }

    Log.d("Soly", imgSelector.toString())

    //Selecciona la imagen basada en el indice
    val selectedImage = solyImages.getOrElse(displayedImageIndex % solyImages.size) {
        R.drawable.monstruo
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Muestra el GIF sobre la imagen cuando se activa
        AnimatedVisibility(
            visible = showGif,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            MostrarGif(modifier = Modifier.size(300.dp))
        }

        // Muestra la imagen solo cuando el GIF no esta presente
        AnimatedVisibility(
            visible = !showGif,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Image(
                painter = painterResource(id = selectedImage),
                contentDescription = "Soly",
                modifier = Modifier.size(300.dp)
            )
        }
    }
}

