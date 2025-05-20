package com.example.tesis.vista.componente

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
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    val imageStep = currentStep / 5

    var hasStartedOnce by remember { mutableStateOf(false) }

    var showGif by remember { mutableStateOf(false) }

    // Index of the image currently shown
    var displayedImageIndex by remember { mutableStateOf(0) }

    val imgSelector = Random.nextInt(2)

    // Track when we need to animate
    LaunchedEffect(imageStep) {
        if (hasStartedOnce && imageStep != displayedImageIndex) {
            showGif = true
            delay(800)
            displayedImageIndex = imageStep
            showGif = false
        } else {
            hasStartedOnce = true
            displayedImageIndex = imageStep
        }
    }

    val solyPirata = listOf(
        R.drawable.monstruo,
        R.drawable.monstruo_1,
        R.drawable.monstruo_1_2,
        R.drawable.monstruo_1_3
    )

    val solyCaballero = listOf(
        R.drawable.monstruo,
        R.drawable.monstruo_2,
        R.drawable.monstruo_2_2,
        R.drawable.monstruo_2_3
    )

    val solyAstronauta = listOf(
        R.drawable.monstruo,
        R.drawable.monstruo_1,
        R.drawable.monstruo_1_2,
        R.drawable.monstruo
    )
    val solyImages = remember {
        when (imgSelector) {
            0 -> solyPirata
            1 -> solyCaballero
            3 -> solyAstronauta
            else -> listOf(R.drawable.monstruo)
        }
    }

    val selectedImage = solyImages.getOrElse(displayedImageIndex % solyImages.size) {
        R.drawable.monstruo
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Show GIF on top when active
        AnimatedVisibility(
            visible = showGif,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            MostrarGif(modifier = Modifier.size(300.dp))
        }

        // Show image only when GIF is not showing
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

