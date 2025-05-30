package com.example.tesis.vista.pantalla

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.tesis.vista.util.ValidadorUtils
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
    val cantidadPreguntas = preguntaDao.getCantidadPreguntas()


    var idResultado by remember{ mutableIntStateOf(resultadoPrueba?.id ?: 0) }
    var idPregunta by remember{mutableStateOf(resultadoDao.getResultadoById(idResultado)?.preguntaId)}

    var pregunta by remember {mutableStateOf(preguntaDao.getPreguntaById(idPregunta?.toInt() ?: 0))}

    var resultado by remember { mutableStateOf(resultadoDao.getResultadoById(idResultado)) }

    var potLing by remember { mutableIntStateOf(0) }
    var potMus by remember { mutableIntStateOf(0) }
    var potIntra by remember { mutableIntStateOf(0) }
    var contLing by remember { mutableIntStateOf(0) }
    var contMat by remember { mutableIntStateOf(0) }
    var contEsp by remember { mutableIntStateOf(0) }
    var contMus by remember { mutableIntStateOf(0) }
    var contCor by remember { mutableIntStateOf(0) }
    var contInter by remember { mutableIntStateOf(0) }
    var contIntra by remember { mutableIntStateOf(0) }
    var contNatu by remember { mutableIntStateOf(0) }

    /**Revisa las respuestas obtenidos y cuenta las respuestas correctas de cada categoria para
     * calcular los resultados del usuario*/
    val validador = ValidadorUtils()

    LaunchedEffect(Unit) {
        for (i in 1..cantidadPreguntas) {
            pregunta = preguntaDao.getPreguntaById(idPregunta ?: 0)
            resultado = resultadoDao.getResultadoById(idResultado)

            val isCorrect = when (pregunta?.tipoPregunta) {
                2 -> validador.isPatronCorrecto(
                    resultado?.respuesta ?: "",
                    pregunta?.respuestaCorrecta ?: ""
                )
                4 ->
                    if(idPregunta == 17){
                        validador.isMinimoItemsCorrectos(
                            resultado?.respuesta ?: "",
                            pregunta?.respuestaCorrecta ?: "",
                            10
                        )
                    } else{
                        validador.isArrastrarYUbicarCorrecto(
                            resultado?.respuesta ?: "",
                            pregunta?.respuestaCorrecta ?: ""
                        )
                    }
                5 ->
                    if(idPregunta == 40){
                        validador.isMinimoItemsCorrectos(
                            resultado?.respuesta ?: "",
                            pregunta?.respuestaCorrecta ?: "",
                            4
                        )
                    } else{
                        validador.isArrastrarYUbicarCorrecto(
                            resultado?.respuesta ?: "",
                            pregunta?.respuestaCorrecta ?: ""
                        )
                    }
                else -> validador.compararSoloTexto(
                    resultado?.respuesta ?: "",
                    pregunta?.respuestaCorrecta ?: ""
                )
            }

            if (isCorrect) {
                when (pregunta?.categoriaId) {
                    1 ->
                        if(idPregunta == 1){
                            potLing += 1
                        }else{
                            contLing += 1
                        }
                    2 -> contMat += 1
                    3 -> contEsp += 1
                    4 ->
                        if(idPregunta == 2){
                            potMus += 1
                        }else{
                            contMus += 1
                        }
                    5 -> contCor += 1
                    6 -> contInter += 1
                    7 ->
                        if(idPregunta == 2){
                            potIntra += 1
                        }else{
                            contIntra += 1
                        }
                    8 -> contNatu += 1
                }
            }
            Log.d("Validation", "Expected: ${pregunta?.respuestaCorrecta}")
            Log.d("Validation", "Given: ${resultado?.respuesta}")
            Log.d("isCorrect", isCorrect.toString())


            idPregunta = idPregunta?.plus(1)
            idResultado += 1
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
                    //Pinta la grafica de resultados en pantalla
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


