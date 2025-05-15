package com.example.tesis.vista.pantalla

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tesis.logica.DbClass
import com.example.tesis.vista.componente.GraficaResultados
import com.example.tesis.vista.util.Mogra
import com.example.tesis.vista.viewmodel.SharedViewModel

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
                    GraficaResultados(
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


