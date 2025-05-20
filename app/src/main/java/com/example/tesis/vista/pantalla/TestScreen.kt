package com.example.tesis.vista.pantalla

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tesis.logica.DbClass
import com.example.tesis.logica.Resultado
import com.example.tesis.vista.component.PreguntaArrastrarYUbicar
import com.example.tesis.vista.componente.AnimacionImagenPorNivel
import com.example.tesis.vista.componente.PreguntaPatronInteractivo
import com.example.tesis.vista.componente.PreguntaRespuestaAbierta
import com.example.tesis.vista.componente.SeleccionMultiple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.tesis.vista.componente.StepsProgressBar
import com.example.tesis.vista.util.FormatUtil
import com.example.tesis.vista.viewmodel.SharedViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestScreen(
    navController: NavController,
    context: Context,
    sharedViewModel: SharedViewModel
) {

    val db = remember { DbClass.getDatabase(context) }
    val preguntaDao = db.preguntaDao()
    val respuestaDao = db.respuestaDao()
    val resultadoDao = db.resultadoDao()

    val estudiante = sharedViewModel.estudiante

    var idPregunta by remember{ mutableIntStateOf(1) }
    var idRespuesta by remember{ mutableIntStateOf(1) }

    var preguntaText by remember { mutableStateOf("")}
    var tipoPregunta by remember { mutableIntStateOf(0) }
    val cantidadPreguntas = preguntaDao.getCantidadPreguntas()

    var respuesta by remember {mutableStateOf("")}

    val coroutineScope = rememberCoroutineScope()

    var localFocusManager = LocalFocusManager.current

    //Contador de pregunta actual para rastrear progreso
    var currentStep by remember { mutableIntStateOf(0) }

    val radioOptions by remember(respuesta) { mutableStateOf(respuesta.split(",")) }
    val (selectedOption, onOptionSelected) = remember(idPregunta) { mutableStateOf<String?>(null) }
    var userAnswers by remember(idPregunta, radioOptions) {
        mutableStateOf(List(radioOptions.size) { "" })
    }

    var userPattern by remember { mutableStateOf("") }

    var userDragMatchResult by remember { mutableStateOf("") }

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val current = LocalDateTime.now().format(formatter)

    var mostrarAlerta by remember { mutableStateOf(false) }


    val amigosSoly = sharedViewModel.amigosSoly
    val assignedStatus = sharedViewModel.assignedStatus


    //Actualizar dinamicamente variables cambiantes
    LaunchedEffect(idPregunta, idRespuesta) {
        val pregunta = preguntaDao.getPreguntaById(idPregunta)
        preguntaText = pregunta?.pregunta ?: ""
        tipoPregunta = pregunta?.tipoPregunta ?: 0
        respuesta = respuestaDao.getRespuestaById(idRespuesta)
    }
    LaunchedEffect(amigosSoly) {
        snapshotFlow {
            amigosSoly.map { it.assignedCell }
        }.collect {
            userDragMatchResult = FormatUtil.formatDragMatchResult(amigosSoly)
        }
    }

    if (mostrarAlerta) {
        AlertDialog(
            onDismissRequest = { mostrarAlerta = false },
            title = { Text("Atención") },
            text = { Text("Debes responder la pregunta para poder continuar") },
            confirmButton = {
                Button(onClick = {
                    mostrarAlerta = false
                    // Acción de confirmación
                }) {
                    Text("Aceptar")
                }
            }
        )
    }

    val isAnswerGiven by remember(
        tipoPregunta,
        selectedOption,
        userPattern,
        userAnswers,
        assignedStatus
    ){
        derivedStateOf {
            when (tipoPregunta) {
                1 -> selectedOption != null
                2 -> userPattern.isNotBlank()
                3 -> userAnswers.all { it.isNotBlank() }
                4 -> assignedStatus.all { it }
                else -> false
            }
        }
    }
    LaunchedEffect(isAnswerGiven) {
        Log.d("Validation", "isAnswerGiven = $isAnswerGiven")
    }

    fun saveRespuesta(preguntaIdSnapshot: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pregunta = db.preguntaDao().getPreguntaById(preguntaIdSnapshot)
                val answerToSave = when (tipoPregunta) {
                    1 -> selectedOption ?: ""
                    2 -> userPattern
                    3 -> userAnswers.joinToString(",")
                    4 -> FormatUtil.formatDragMatchResult(amigosSoly)
                    else -> ""
                }
                if (estudiante != null && pregunta != null) {
                    val resultado = Resultado(
                        estudianteId = estudiante.id,
                        preguntaId = preguntaIdSnapshot,
                        respuesta = answerToSave,
                        fecha = current
                    )
                    val idInsertado = resultadoDao.insertResultado(resultado).toInt()
                    val resultadoConId = resultado.copy(id = idInsertado)
                    Log.d("DB", "Resultado insertado con éxito: $resultadoConId")

                    /**Guarda el id de la primera pregunta que el usuario responde para
                     * poder validar las respuestas desde la primera respuesta dada en la
                     * prueba actual*/
                    if (preguntaIdSnapshot == 1) {
                        withContext(Dispatchers.Main) {
                            sharedViewModel.resultado = resultadoConId
                            Log.d("DB", "Guardado en ViewModel con ID: ${resultadoConId.id}")
                        }
                    }
                }else {
                    Log.e("DB", "Estudiante o pregunta no existe — insert cancelado")
                }
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error guardando resultado: ${e.message}", e)
            }
        }
    }

    Surface (modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        },
        color = Color(0xFFFFFF99)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            //Columna contenedora
            Column(modifier = Modifier.fillMaxHeight().weight(0.9f)) {
                //Fila superior para mapa y Soly
                Row(
                    modifier = Modifier.fillMaxWidth().weight(0.3f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    //Columna mapa
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ){
                        StepsProgressBar(
                            modifier = Modifier.fillMaxWidth(),
                            numberOfSteps = cantidadPreguntas,
                            currentStep = currentStep
                        )
                    }
                    //Columna Soly
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        AnimacionImagenPorNivel(
                            currentStep = currentStep,
                            modifier = Modifier
                                .size(300.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                //Fila pregunta
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    //Seleccion de pregunta basada en tipo
                    when (tipoPregunta) {
                        1-> {
                            // Preguntas seleccion multiple
                            SeleccionMultiple(
                                context = context,
                                idPregunta = idPregunta,
                                questionText = preguntaText,
                                radioOptions = radioOptions,
                                selectedOption = selectedOption,
                                onOptionSelected = onOptionSelected
                            )
                        }
                        2 -> {
                            // Pregunta de patron interactivo
                            PreguntaPatronInteractivo(
                                context = context,
                                idPregunta = idPregunta,
                                preguntaText = preguntaText,
                                currentPattern = userPattern,
                                onPatternChanged = { userPattern = it },
                            )
                        }
                        3 -> {
                            // Pregunta de respuesta abierta
                            PreguntaRespuestaAbierta(context = context,
                                idPregunta = idPregunta,
                                preguntaText = preguntaText,
                                radioOptions = radioOptions,
                                respuestas = userAnswers,
                                onAnswersChanged = { userAnswers = it }
                            )
                        }
                        4 -> {
                            // Pregunta de mover objetos
                            PreguntaArrastrarYUbicar(
                                context = context,
                                idPregunta = idPregunta,
                                preguntaText = preguntaText,
                                items = amigosSoly,
                                gridSize = 4,
                                onAssignmentChanged = { statusList ->
                                    assignedStatus.clear()
                                    assignedStatus.addAll(statusList)
                                }
                            )
                        }
                    }
                }
            }
            Box(modifier = Modifier.weight(0.1f).fillMaxHeight()) {

                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        localFocusManager.clearFocus()
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                /**Valida el avance de las preguntas para mostrar el paso actual del
                                 * usuario, y valida que la pregunta haya sido respondida antes de
                                 * avanzar*/
                                if (currentStep < cantidadPreguntas - 1) {
                                    if (isAnswerGiven) {
                                        val currentId = idPregunta
                                        saveRespuesta(currentId)
                                        currentStep++
                                        idPregunta++
                                        idRespuesta++
                                    } else {
                                        mostrarAlerta = true
                                    }
                                }

                                else if (currentStep < cantidadPreguntas) {
                                    if (isAnswerGiven) {
                                        currentStep = currentStep + 1
                                        saveRespuesta(idPregunta)
                                    } else {
                                        mostrarAlerta = true
                                    }
                                } else if (currentStep == cantidadPreguntas) {
                                    withContext(Dispatchers.Main) {
                                        Log.d("res", sharedViewModel.resultado.toString())
                                        navController.navigate("results")
                                    }
                                }
                            }
                        }
                    }
                ) {
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
