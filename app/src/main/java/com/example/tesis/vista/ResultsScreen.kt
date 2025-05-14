package com.example.tesis.vista

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tesis.R
import com.example.tesis.logica.DbClass
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ResultsScreen(
    navController: NavHostController,
    context: Context,
    sharedViewModel: SharedViewModel
) {

    val db = remember { DbClass.getDatabase(context) }
    val preguntaDao = db.preguntaDao()
    val resultadoDao = db.resultadoDao()
    val estudiante = sharedViewModel.estudiante
    val resultadoPrueba = sharedViewModel.resultado


    var idResultado by remember{mutableStateOf(resultadoPrueba?.id ?: 0)}
    var idPregunta by remember{mutableStateOf(resultadoDao.getResultadoById(idResultado)?.preguntaId)}

    var pregunta by remember {mutableStateOf(preguntaDao.getPreguntaById(idPregunta?.toInt() ?: 0))}

    var resultado by remember { mutableStateOf(resultadoDao.getResultadoById(idResultado)) }

    var contLing by remember { mutableStateOf(0) }
    var contMat by remember { mutableStateOf(0) }
    var contEsp by remember { mutableStateOf(0) }
    var contMus by remember { mutableStateOf(0) }
    var contCor by remember { mutableStateOf(0) }
    var contInter by remember { mutableStateOf(0) }
    var contIntra by remember { mutableStateOf(0) }
    var contNatu by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in 1..8) {

            pregunta = preguntaDao.getPreguntaById(idPregunta ?: 0)
            resultado = resultadoDao.getResultadoById(idResultado)
            if(pregunta?.respuestaCorrecta == resultado?.respuesta){
                when (pregunta?.categoriaId) {
                    1 -> contLing = contLing + 1
                    2 -> contMat = contMat + 1
                    3 -> contEsp = contEsp + 1
                    4 -> contMus = contMus + 1
                    5 -> contCor = contCor + 1
                    6 -> contInter = contInter + 1
                    7 -> contIntra = contIntra + 1
                    8 -> contNatu = contNatu + 1
                }
                idPregunta = idPregunta?.plus(1)
                idResultado = idResultado + 1
            }else{
                idPregunta = idPregunta?.plus(1)
                idResultado = idResultado + 1
            }
            /*Log.d("idpregunsum", idPregunta.toString())
            Log.d("pregun", pregunta?.categoriaId.toString())
            Log.d("Respcor", pregunta?.respuestaCorrecta.toString())
            Log.d("Resp", resultado?.respuesta.toString())
            Log.d("Esp", contEsp.toString())
            Log.d("Mus", contMus.toString())
            Log.d("Cor", contCor.toString())
            Log.d("Inter", contInter.toString())
            Log.d("Intra", contIntra.toString())
            Log.d("Natu", contNatu.toString())*/
        }
    }

    Surface(
        modifier = Modifier
        .fillMaxSize(),
        color = Color(0xFFFFFF99)
    ){
        Row (modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier.weight(0.9f)
                    .fillMaxSize()
                    .padding(horizontal = 100.dp, vertical = 100.dp)
                    .verticalScroll(rememberScrollState())

            ){
                Text(
                    text = "Resultados de ${estudiante?.nombre}:",
                    fontSize = 75.sp,
                    modifier = Modifier.padding(bottom = 20.dp),
                    fontFamily = Mogra

                )
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                    IntelligenceRadarChart(
                        scores = listOf(
                            contLing, contMat, contEsp, contMus,
                            contCor, contInter, contIntra, contNatu
                        )
                    )
                }
            }
            Box(modifier = Modifier.weight(0.1f).fillMaxHeight()){
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        sharedViewModel.contLing = contLing
                        sharedViewModel.contMat = contMat
                        sharedViewModel.contEsp = contEsp
                        sharedViewModel.contMus = contMus
                        sharedViewModel.contCor = contCor
                        sharedViewModel.contInter = contInter
                        sharedViewModel.contIntra = contIntra
                        sharedViewModel.contNatu = contNatu
                        navController.navigate("recommendations")
                    }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Siguiente",
                        tint = Color.Black,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun IntelligenceRadarChart(
    scores: List<Int>,
    maxScore: Int = 5,
    modifier: Modifier = Modifier
) {
    require(scores.size == 8) { "Debe poner solo ocho inteligencias" }

    Canvas(modifier = modifier.size(400.dp)) {
        val center = this.center
        val radius = size.minDimension / 2 * 0.8f
        val angleStep = (2 * Math.PI / 8).toFloat()

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
                val scoreFraction = rawScoreFraction.coerceAtLeast(0.05f) // Radio minimo de 5% para mostrar
                val scaledRadius = radius * scoreFraction
                val angle = i * angleStep - Math.PI / 2
                val x = center.x + scaledRadius * cos(angle).toFloat()
                val y = center.y + scaledRadius * sin(angle).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }


        drawPath(path = filledPath, color = Color(0xFF55DFBE).copy(alpha = 1f))

        // Dibuja las lineas desde el centro hasta el vertice dibujado
        for (i in 0 until 8) {
            val scoreFraction = (scores[i].toFloat() / maxScore).coerceAtLeast(0.05f)

            val scaledRadius = radius * scoreFraction
            val angle = i * angleStep - Math.PI / 2
            val x = center.x + scaledRadius * cos(angle).toFloat()
            val y = center.y + scaledRadius * sin(angle).toFloat()
            //Agrega un punto en los vertices de cada puntuacion
            //drawCircle(Color.Red, radius = 6f, center = Offset(x, y))
        }

        //Etiquetas de texto para cada inteligencia
        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = android.graphics.Paint.Align.CENTER
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
                    y + index * 22f, // 22f is line height spacing
                    textPaint
                )
            }
        }

    }
}
