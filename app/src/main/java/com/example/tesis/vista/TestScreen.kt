package com.example.tesis.vista

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.tesis.R
import com.example.tesis.logica.DbClass
import com.example.tesis.logica.Resultado
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TestScreen(
    navController: NavController,
    context: Context,
    sharedViewModel: SharedViewModel
) {

    // Importar fuente para letras
    val mogra = FontFamily(
        Font(R.font.mogra_regular, FontWeight.Light),
        Font(R.font.mogra_regular, FontWeight.Normal),
        Font(R.font.mogra_regular, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.mogra_regular, FontWeight.Medium),
        Font(R.font.mogra_regular, FontWeight.Bold)
    )

    val db = remember { DbClass.getDatabase(context) }
    val preguntaDao = db.preguntaDao()
    val respuestaDao = db.respuestaDao()
    val resultadoDao = db.resultadoDao()

    val estudiante = sharedViewModel.estudiante

    var idPregunta by remember{mutableStateOf(1)}
    var idRespuesta by remember{mutableStateOf(1)}

    var preguntaText by remember { mutableStateOf("")}
    val cantidadPreguntas = preguntaDao.getCantidadPreguntas()

    var respuesta by remember {mutableStateOf("")}

    //Contador de pregunta actual para rastrear progreso
    var currentStep by remember { mutableStateOf(0) }

    val radioOptions by remember(respuesta) { mutableStateOf(respuesta.split(",")) }
    val (selectedOption, onOptionSelected) = remember(idPregunta) { mutableStateOf<String?>(null) }

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val current = LocalDateTime.now().format(formatter)

    var mostrarAlerta by remember { mutableStateOf(false) }

    //FocusManager para cerrar el teclado en pantalla al tocar fuera
    var localFocusManager = LocalFocusManager.current

    //Actualizar pregunta, respuesta y seleccion dinamicamente
    LaunchedEffect(idPregunta, idRespuesta) {
        preguntaText = preguntaDao.getTextoPreguntaById(idPregunta)
        respuesta = respuestaDao.getRespuestaById(idRespuesta)
        //selectedOption = null // clear selection for new question
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

    fun saveRespuesta() {
        CoroutineScope(Dispatchers.IO).launch IconButton@{
            try {
                val pregunta = db.preguntaDao().getPreguntaById(idPregunta)

                Log.d("Preg", pregunta.toString())
                Log.d("DEBUG", "idEstudiante = ${estudiante?.id.toString()}, idPregunta = $idPregunta")
                if (estudiante != null && pregunta != null) {
                    val resultado = Resultado(
                        estudianteId = estudiante.id,
                        preguntaId = idPregunta,
                        respuesta = selectedOption.toString(),
                        fecha = current.toString()
                    )
                    resultadoDao.insertResultado(resultado)
                    Log.d("DB", "Resultado insertado con éxito")
                } else {
                    Log.e("DB", "Estudiante o pregunta no existe — insert cancelado")
                }

            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error guardando resultado: ${e.message}", e)
            }
        }
    }

    Surface (modifier = Modifier
        .fillMaxSize(),
        color = Color(0xFFFFFF99)
    ){
        Row(modifier = Modifier.fillMaxSize()){
        //Columna contenedora
            Column (modifier = Modifier.fillMaxHeight().weight(0.9f)){
                //Fila superior para mapa y Soly
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ){

                    //Columna mapa
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {

                        StepsProgressBar(modifier = Modifier.fillMaxWidth(), numberOfSteps = cantidadPreguntas, currentStep = currentStep)
                    }

                    //Columna Soly
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        //GifPreview()
                        Image(
                            painter = painterResource(id = R.drawable.monstruo_2),
                            contentDescription = "Soly",
                            modifier = Modifier
                                .size(300.dp)
                        )
                    }
                }
                //Fila enunciado pregunta
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    AnimatedContent(
                        targetState = preguntaText,
                        transitionSpec = {
                            slideInHorizontally(
                                                    animationSpec = tween(500),
                                                    initialOffsetX = { fullWidth -> fullWidth } // slide from right
                                                ).togetherWith(
                                slideOutHorizontally(
                                                    animationSpec = tween(500),
                                                    targetOffsetX = { fullWidth -> -fullWidth } // slide to left
                                                ))
                        },
                        label = "PreguntaSlide"
                    ) { pregunta ->
                    Text(
                        text = pregunta,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF34D9A2),
                        fontFamily = mogra,
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(12.dp)
                    )}
                }
                //Fila imagenes pregunta
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Small weight for image
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Crossfade(
                        targetState = idPregunta,
                        label = "TransicionImagen"
                    ) { targetIdPregunta ->

                        val imageName = "p$targetIdPregunta" // ejemplo: "p1", "p2", "p3"
                        val imageResId = context.resources.getIdentifier(
                            imageName,
                            "drawable",
                            context.packageName
                        )

                        val validImage = if (imageResId != 0) imageResId else R.drawable.monstruo_2

                        Image(
                            painter = painterResource(id = validImage),
                            contentDescription = "Imagen para pregunta $targetIdPregunta",
                            modifier = Modifier
                                .size(400.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
                //Fila respuestas pregunta
                Row(
                    modifier = Modifier.weight(0.5f).selectableGroup()
                ) {
                    radioOptions.forEach {
                        text->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .selectable(selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RadioButton(
                                selected = (text==selectedOption),
                                onClick = { onOptionSelected(text) }
                            )
                            Text(
                                text = text,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


            }
            Box (modifier = Modifier.weight(0.1f).fillMaxHeight()){

                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {

                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d("estuId", estudiante?.id.toString())

                            if(idPregunta == 1){
                                val res = Resultado(
                                    estudianteId = estudiante?.id ?: 0,
                                    preguntaId = idPregunta,
                                    respuesta = selectedOption.toString(),
                                    fecha = current.toString()
                                )
                                val id = db.resultadoDao().insertResultado(res).toInt()
                                val resWithId = res.copy(id = id)
                                sharedViewModel.resultado = resWithId
                            }
                            if (currentStep<cantidadPreguntas-1){
                                if(selectedOption != null){
                                    currentStep = currentStep + 1
                                    idPregunta = idPregunta +1
                                    idRespuesta = idRespuesta +1
                                    saveRespuesta()
                                }else{
                                    mostrarAlerta = true
                                }

                            }else if(currentStep<cantidadPreguntas){
                                currentStep = currentStep +1
                            }else if(currentStep == cantidadPreguntas){
                                withContext(Dispatchers.Main) {
                                    Log.d("res", sharedViewModel.resultado.toString())
                                    navController.navigate("results")
                                }
                            }
                        }

                        Log.d("Id Pregunta", idPregunta.toString())
                        Log.d("Cantidad preguntas", cantidadPreguntas.toString())
                        Log.d("Pregunta actual", currentStep.toString())
                        Log.d("respuesta", respuesta.toString())
                        Log.d("Id respuesta", idRespuesta.toString())
                        Log.d("id estud", estudiante?.id.toString())
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

//Barra de progreso
@Composable
fun Step(modifier: Modifier = Modifier, isComplete: Boolean, isCurrent: Boolean) {
    val color = if (isComplete || isCurrent) Color(0xFF55DFBE) else Color.LightGray
    val innerCircleColor = if (isComplete) Color(0xFFF5B800) else Color.LightGray

    Box(modifier = modifier) {

        //Linea
        Divider(
            modifier = Modifier.align(Alignment.CenterStart),
            color = color,
            thickness = 6.dp
        )

        //Estrella
        StarCanvasSmooth(
            modifier = Modifier.size(100.dp),
            fillColor = innerCircleColor,
            borderColor = innerCircleColor
        )
    }
}

@Composable
fun StepsProgressBar(modifier: Modifier = Modifier, numberOfSteps: Int, currentStep: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..numberOfSteps) {
            Step(
                modifier = Modifier.weight(1F),
                isComplete = step <= currentStep,
                isCurrent = step == currentStep
            )
        }
    }
}

//Forma de estrella
@Composable
fun StarCanvasSmooth(
    modifier: Modifier = Modifier,
    fillColor: Color,
    borderColor: Color,
    //strokeWidth: Float = 4f,
    cornerRadius: Float = 30f,
    points: Int = 5
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val outerRadius = size.minDimension / 2.2f
        val innerRadius = outerRadius / 2.5f
        val starPath = createStarPath(center, outerRadius, innerRadius, points)

        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                this.color = fillColor
                this.style = PaintingStyle.Fill
                this.pathEffect = PathEffect.cornerPathEffect(cornerRadius)
            }
            canvas.drawPath(starPath, paint)

            val strokePaint = Paint().apply {
                this.color = borderColor
                this.style = PaintingStyle.Stroke
                //this.strokeWidth = strokeWidth
                this.pathEffect = PathEffect.cornerPathEffect(cornerRadius)
            }
            canvas.drawPath(starPath, strokePaint)
        }
    }
}

//Creacion manual de la estrella
private fun createStarPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int
): Path {
    val path = Path()
    val angleStep = 360.0 / (points * 2)

    //For para dibujar las lineas de la estrella basado en cantidad de puntas
    for (i in 0 until points * 2) {
        val angleDegrees = i * angleStep - 90
        val angleRadians = Math.toRadians(angleDegrees)
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val x = (center.x + radius * cos(angleRadians)).toFloat()
        val y = (center.y + radius * sin(angleRadians)).toFloat()

        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }

    path.close()
    return path
}

//Loader para poder mostrar GIFs en pantalla
@Composable
fun GifLoader(resourceId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    AsyncImage(
        model = resourceId,
        contentDescription = "GIF animado",
        modifier = modifier,
        imageLoader = imageLoader
    )
}

//Carga el gif utilizando el loader creado
@Composable
fun GifPreview() {

        GifLoader(
            resourceId = R.drawable.smoke_gif,
            modifier = Modifier.size(200.dp)
        )

}



