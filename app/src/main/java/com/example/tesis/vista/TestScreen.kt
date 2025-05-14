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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.TextField
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import kotlin.math.hypot
import kotlin.math.min


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
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

    var idPregunta by remember{mutableStateOf(1)}
    var idRespuesta by remember{mutableStateOf(1)}

    var preguntaText by remember { mutableStateOf("")}
    val cantidadPreguntas = preguntaDao.getCantidadPreguntas()

    var respuesta by remember {mutableStateOf("")}

    val coroutineScope = rememberCoroutineScope()


    //Contador de pregunta actual para rastrear progreso
    var currentStep by remember { mutableStateOf(0) }

    val radioOptions by remember(respuesta) { mutableStateOf(respuesta.split(",")) }
    val (selectedOption, onOptionSelected) = remember(idPregunta) { mutableStateOf<String?>(null) }
    var userAnswers by remember { mutableStateOf(List(radioOptions.size) { "" }) }
    var userPattern by remember { mutableStateOf("") }

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val current = LocalDateTime.now().format(formatter)

    var mostrarAlerta by remember { mutableStateOf(false) }

    //Actualizar pregunta, respuesta y seleccion dinamicamente
    LaunchedEffect(idPregunta, idRespuesta) {
        preguntaText = preguntaDao.getTextoPreguntaById(idPregunta)
        respuesta = respuestaDao.getRespuestaById(idRespuesta)
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
                    modifier = Modifier.fillMaxWidth().weight(0.3f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ){

                    //Columna mapa
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {

                        StepsProgressBar(
                            modifier = Modifier.fillMaxWidth(),
                            numberOfSteps = cantidadPreguntas,
                            currentStep = currentStep)
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
                    /*SeleccionMultiple(
                        context = context,
                        idPregunta = idPregunta,
                        questionText = preguntaText,
                        radioOptions = radioOptions,
                        selectedOption = selectedOption,
                        onOptionSelected = onOptionSelected
                    )

                    RespuestaAbierta(context = context,
                        idPregunta = 4,
                        preguntaText = preguntaText,
                        radioOptions = radioOptions,
                        respuestas = userAnswers,
                        onAnswersChanged = { userAnswers = it }
                    )*/

                    PreguntaPatron(
                        context = context,
                        idPregunta = idPregunta,
                        preguntaText = preguntaText,
                        respuestaPatron = userPattern,
                        onPatternChanged = { userPattern = it },
                        gridSize = 4
                    )


                    /*AnimatedContent(
                        targetState = preguntaText,
                        transitionSpec = {
                            slideInHorizontally(
                                                    animationSpec = tween(500),
                                                    //deslizar desde la derecha
                                                    initialOffsetX = { fullWidth -> fullWidth }
                                                ).togetherWith(
                                slideOutHorizontally(
                                                    animationSpec = tween(500),
                                                    //deslizar hacia la izquierda
                                                    targetOffsetX = { fullWidth -> -fullWidth }
                                                ))
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
                    )}
                }
                //Fila imagenes pregunta
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imageName = "p$idPregunta"
                    val imageResId = context.resources.getIdentifier(
                        imageName,
                        "drawable",
                        context.packageName
                    )
                    val validImage = if (imageResId != 0) imageResId else R.drawable.monstruo_2

                    //Overlay del patron de imagen para pregunta 13
                    if (idPregunta == 13) {
                        Box(modifier = Modifier.size(400.dp)) {
                            Image(
                                painter = painterResource(id = validImage),
                                contentDescription = "Imagen para pregunta $idPregunta",
                                modifier = Modifier.fillMaxSize()
                            )

                            PatronPregunta(
                                columns = 6,
                                rows = 7,
                                modifier = Modifier.fillMaxSize(),
                                onPatternComplete = { pattern ->
                                    onOptionSelected(pattern)
                                    Log.d("PatronPregunta", "User pattern: $pattern")
                                }
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(id = validImage),
                            contentDescription = "Imagen para pregunta $idPregunta",
                            modifier = Modifier.size(400.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
                //Fila respuestas pregunta
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    when (idPregunta) {
                        10, 11, 12 -> {
                            var textInput by remember(idPregunta) { mutableStateOf("") }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextField(
                                    value = textInput,
                                    onValueChange = {
                                        textInput = it
                                        onOptionSelected(it)
                                    },
                                    label = { Text("Escribe tu respuesta") },
                                    singleLine = true
                                )
                            }
                        }
                        else -> {
                            // Default — radio buttons
                            Row(
                                modifier = Modifier.selectableGroup()
                            ) {
                                radioOptions.forEach { text ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .selectable(
                                                selected = (text == selectedOption),
                                                onClick = { onOptionSelected(text) },
                                                role = Role.RadioButton
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        RadioButton(
                                            selected = (text == selectedOption),
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
                    }*/
                }
            }
            Box (modifier = Modifier.weight(0.1f).fillMaxHeight()){

                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        coroutineScope.launch {
                            Log.d("estuId", estudiante?.id.toString())
                            withContext(Dispatchers.IO) {
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
//Preguntas seleccion multiple
@Composable
fun SeleccionMultiple(
    context: Context,
    idPregunta: Int,
    questionText: String,
    radioOptions: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Texto animado
        AnimacionTexto(questionText)

        Spacer(modifier = Modifier.height(16.dp))

        ImagenPregunta(
            idPregunta = idPregunta,
            modifier = Modifier
                .size(300.dp)
                .padding(vertical = 16.dp)
        )


        Row(
            modifier = Modifier.selectableGroup()
        ) {
            radioOptions.forEach { text ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
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
}
//Pregunta abierta
@Composable
fun RespuestaAbierta(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    radioOptions: List<String>,
    respuestas: List<String>,
    onAnswersChanged: (List<String>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        Spacer(modifier = Modifier.height(16.dp))

        ImagenPregunta(
            idPregunta = idPregunta,
            modifier = Modifier
                .size(300.dp)
                .padding(vertical = 16.dp)
        )


        Row {
            radioOptions.forEachIndexed { index, label ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    OutlinedTextField(
                        value = respuestas.getOrNull(index) ?: "",
                        onValueChange = { newText ->
                            val nuevaRespuesta = respuestas.toMutableList()
                            if (nuevaRespuesta.size <= index) {
                                nuevaRespuesta.addAll(List(index - nuevaRespuesta.size + 1) { "" })
                            }
                            nuevaRespuesta[index] = newText
                            onAnswersChanged(nuevaRespuesta)
                        },
                        //label = { Text(label.ifBlank { "Respuesta ${index + 1}" }) },
                        singleLine = true,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(100.dp)
                    )
                }
            }
        }
    }
}
//Pregunta patron
@Composable
fun PreguntaPatron(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    respuestaPatron: String,
    onPatternChanged: (String) -> Unit,
    gridSize: Int = 3
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        Spacer(modifier = Modifier.height(16.dp))

        // Image and Pattern side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImagenPregunta(
                context = context,
                idPregunta = idPregunta,
                modifier = Modifier
                    .size(250.dp)
                    .padding(8.dp)
            )

            FlexiblePatternGrid(
                gridSize = gridSize,
                initialPattern = respuestaPatron,
                onPatternCompleted = onPatternChanged,
                modifier = Modifier
                    .size(250.dp) // fixed size matching image
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onPatternChanged("") }) {
            Text("Limpiar patrón")
        }
    }
}

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
//Cargar imagen dinamicamente basado en el id de la pregunta
@Composable
fun ImagenPregunta(
    context: Context = LocalContext.current,
    idPregunta: Int,
    modifier: Modifier = Modifier
) {
    val imageName = "p$idPregunta"
    val imageResId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )
    val validImage = if (imageResId != 0) imageResId else R.drawable.monstruo_2

    Image(
        painter = painterResource(id = validImage),
        contentDescription = "Imagen para pregunta $idPregunta",
        modifier = modifier
    )
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
fun MostrarGif() {
        GifLoader(
            resourceId = R.drawable.smoke_gif,
            modifier = Modifier.size(200.dp)
        )
}
//Patron para pregunta copiar imagen
@Composable
fun FlexiblePatternGrid(
    gridSize: Int = 3,
    initialPattern: String,
    onPatternCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDots = remember { mutableStateListOf<Int>() }
    val dotCenters = remember { mutableStateListOf<Offset>() }
    val dotRadius = 30f

    LaunchedEffect(initialPattern) {
        selectedDots.clear()
        if (initialPattern.isNotBlank()) {
            selectedDots.addAll(initialPattern.split("-").mapNotNull { it.toIntOrNull() })
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        checkDotSelection(offset, dotCenters, selectedDots, dotRadius)
                    },
                    onDrag = { change, _ ->
                        checkDotSelection(change.position, dotCenters, selectedDots, dotRadius)
                    },
                    onDragEnd = {
                        if (selectedDots.isNotEmpty()) {
                            val patternString = selectedDots.joinToString("-")
                            onPatternCompleted(patternString)
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            dotCenters.clear()
            val spacingX = size.width / (gridSize + 1)
            val spacingY = size.height / (gridSize + 1)

            for (i in 0 until gridSize) {
                for (j in 0 until gridSize) {
                    val x = spacingX * (j + 1)
                    val y = spacingY * (i + 1)
                    val center = Offset(x, y)
                    dotCenters.add(center)

                    drawCircle(
                        color = if (selectedDots.contains(i * gridSize + j)) Color(0xFF55DFBE) else Color.LightGray,
                        radius = dotRadius,
                        center = center,
                        style = Stroke(width = 4f)
                    )
                }
            }

            if (selectedDots.size >= 2) {
                val path = Path().apply {
                    selectedDots.map { dotCenters[it] }.forEachIndexed { index, offset ->
                        if (index == 0) moveTo(offset.x, offset.y)
                        else lineTo(offset.x, offset.y)
                    }
                }
                drawPath(path, color = Color(0xFF55DFBE), style = Stroke(width = 8f))
            }
        }
    }
}


private fun checkDotSelection(
    position: Offset,
    dotCenters: List<Offset>,
    selectedDots: MutableList<Int>,
    dotRadius: Float,
    toleranceFactor: Float = 1.5f
) {
    dotCenters.forEachIndexed { index, center ->
        if (!selectedDots.contains(index)) {
            val distance = hypot(center.x - position.x, center.y - position.y)
            if (distance <= dotRadius * toleranceFactor) {
                selectedDots.add(index)
            }
        }
    }
}

@Composable
fun ImagenArrastrable(
    imageResId: Int,
    contentDescription: String = "",
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
            .size(100.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    //onDragEnd = { onDrop(imageResId) }, // Optional trigger on release
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize()
        )
    }
}