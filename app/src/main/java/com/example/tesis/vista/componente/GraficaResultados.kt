package com.example.tesis.vista.componente

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GraficaResultados(
    modifier: Modifier = Modifier,
    scores: List<Int>,
    maxScore: Int = 5,
) {
    require(scores.size == 8) { "Debe poner solo ocho inteligencias" }

    Canvas(modifier = modifier.size(400.dp)) {
        val center = this.center
        val radius = size.minDimension / 2 * 0.8f
        val angleStep = (2 * Math.PI / 8).toFloat()

        //Etiquetas a mostrar en los vertices del grafico
        val labels = listOf(
            "Inteligencia\nLingüística",
            "Inteligencia\nLógico-Matemática",
            "Inteligencia\nEspacial",
            "Inteligencia\nMusical",
            "Inteligencia\nCorporal-Cinestésica",
            "Inteligencia\nInterpersonal",
            "Inteligencia\nIntrapersonal",
            "Inteligencia\nNaturalista"
        )

        // Borde de la grafica
        val outerPath = Path().apply {
            for (i in 0 until 8) {
                val angle = i * angleStep - Math.PI / 2
                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }

        drawPath(path = outerPath, color = Color.Black, style = Stroke(width = 2f))

        //Llenado de la grafica
        val filledPath = Path().apply {
            for (i in 0 until 8) {
                val rawScoreFraction = scores[i].toFloat() / maxScore
                val scoreFraction = rawScoreFraction.coerceAtLeast(0.05f) /* Radio minimo de 5% para
                                                                       mostrar para que la grafica no aparezca vacia*/
                val scaledRadius = radius * scoreFraction
                val angle = i * angleStep - Math.PI / 2
                val x = center.x + scaledRadius * cos(angle).toFloat()
                val y = center.y + scaledRadius * sin(angle).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }

        //Dibuja el relleno de la grafica
        drawPath(path = filledPath, color = Color(0xFF55DFBE).copy(alpha = 1f))

        // Dibuja las lineas desde el centro hasta el vertice dibujado
        for (i in 0 until 8) {
            val scoreFraction = (scores[i].toFloat() / maxScore).coerceAtLeast(0.05f)

            val scaledRadius = radius * scoreFraction
            val angle = i * angleStep - Math.PI / 2
            //Agrega un punto en los vertices de cada puntuacion
            //val x = center.x + scaledRadius * cos(angle).toFloat()
            //val y = center.y + scaledRadius * sin(angle).toFloat()
            //drawCircle(Color.Red, radius = 6f, center = Offset(x, y))
        }

        //Etiquetas de texto para cada inteligencia
        val textPaint = Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 20f
            isFakeBoldText = true
            isAntiAlias = true
        }

        for (i in 0 until 8) {
            val angle = i * angleStep - Math.PI / 2
            val labelRadius = radius * 1.15f // Separa ligeramente las etiquetas del grafico
            val x = center.x + labelRadius * cos(angle).toFloat()
            val y = center.y + labelRadius * sin(angle).toFloat() + 12f

            val lines = labels[i].split("\n")
            lines.forEachIndexed { index, line ->
                drawContext.canvas.nativeCanvas.drawText(
                    line,
                    x,
                    y + index * 22f, // 22f es el espaciado de altura de la linea
                    textPaint
                )
            }
        }

    }
}