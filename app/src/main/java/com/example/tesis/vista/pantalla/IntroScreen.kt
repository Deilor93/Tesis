package com.example.tesis.vista.pantalla

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tesis.R
import com.example.tesis.logica.DbClass
import android.content.Context
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.tesis.logica.Estudiante
import com.example.tesis.vista.util.Mogra
import com.example.tesis.vista.viewmodel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun IntroScreen(navController: NavController, context: Context, sharedViewModel: SharedViewModel) {
    var nombreEstudiante by remember { mutableStateOf("") }
    var codigoEstudiante by remember { mutableStateOf("") }

    // Instancia de base de datos
    val db = remember { DbClass.getDatabase(context) }

    var localFocusManager = LocalFocusManager.current

    var mostrarAlerta by remember { mutableStateOf(false) }


    Surface (modifier = Modifier
        .fillMaxSize(),
        color = Color(0xFFFFFF99)
    ){
        Column(modifier = Modifier.fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                    })
                }
        ){

            Row (
                modifier = Modifier
                    .fillMaxWidth().weight(0.2f).padding(top = 50.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFFFF5500),
                                fontSize = 100.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Mogra
                            )
                        ) { append("JUEGA ") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF55DFBE),
                                fontSize = 100.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Mogra
                            )
                        ) { append("CON ") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF34D9A2),
                                fontSize = 100.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Mogra
                            )
                        ) { append("SOLY") }
                    }
                )
            }

            Row (modifier = Modifier.weight(0.8f)){

                Spacer(modifier = Modifier.width(300.dp))
                Column (modifier = Modifier.align(Alignment.CenterVertically)){

                    OutlinedTextField(
                        value = nombreEstudiante,
                        onValueChange = { nombreEstudiante = it },
                        label = { Text("Nombre de estudiante") },
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = codigoEstudiante,
                        onValueChange = {
                            codigoEstudiante = it.filter { char -> char.isDigit() }
                        },
                        label = { Text("Código estudiantil") },
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.End),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (mostrarAlerta) {
                        AlertDialog(
                            onDismissRequest = { mostrarAlerta = false },
                            title = { Text("Atención") },
                            text = { Text("Debe ingresar el nombre y el código del estudiante para continuar") },
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

                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    if(nombreEstudiante != "" && codigoEstudiante != ""){
                                        val est = Estudiante(
                                            nombre = nombreEstudiante,
                                            codigo = codigoEstudiante
                                        )
                                        val id = db.estudianteDao().insertEstudiante(est).toInt()
                                        val estWithId = est.copy(id = id)
                                        withContext(Dispatchers.Main) {
                                            sharedViewModel.estudiante = estWithId
                                            navController.navigate("welcome")
                                        }
                                    }else{
                                        mostrarAlerta = true
                                        Log.e("DB", "Estudiante o codigo en blanco — insert cancelado")
                                    }
                                }catch (e: Exception){
                                    Log.e("DB_ERROR", "Error guardando resultado: ${e.message}", e)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF34D9A2)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .align(Alignment.End)
                    ) {
                        Text("Comenzar", color = Color.Black)
                    }
                }

                Column {
                    Image(
                        painter = painterResource(id = R.drawable.monstruo_2),
                        contentDescription = "Soly",
                        modifier = Modifier
                            .size(1000.dp)
                    )
                }
            }
        }
    }
}
