package com.example.tesis.vista.componente

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tesis.vista.util.Mogra

//Animar texto pregunta
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimacionTexto(
    questionText: String
) {
    AnimatedContent(
        targetState = questionText,
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(500),
                initialOffsetX = { fullWidth -> fullWidth }
            ).togetherWith(
                slideOutHorizontally(
                    animationSpec = tween(500),
                    targetOffsetX = { fullWidth -> -fullWidth }
                )
            )
        },
        label = "PreguntaSlide"
    ) { pregunta ->
        Text(
            text = pregunta,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF34D9A2),
            fontFamily = Mogra,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(12.dp)
        )
    }
}