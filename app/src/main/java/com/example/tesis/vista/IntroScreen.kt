package com.example.tesis.vista

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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tesis.R
import com.example.tesis.logica.DbClass
import android.content.Context

@Composable
fun IntroScreen(navigationViewModel: NavigationViewModel, viewModel: EstudianteViewModel) {
    var nombreEstudiante by remember { mutableStateOf("") }
    var codigoEstudiante by remember { mutableStateOf("") }

    val mogra = FontFamily(
        Font(R.font.mogra_regular, FontWeight.Light),
        Font(R.font.mogra_regular, FontWeight.Normal),
        Font(R.font.mogra_regular, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.mogra_regular, FontWeight.Medium),
        Font(R.font.mogra_regular, FontWeight.Bold)
    )

    Surface (modifier = Modifier
        .fillMaxSize(),
        color = Color(0xFFFFFF99)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth().absoluteOffset(0.dp, 50.dp),
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
                            fontFamily = mogra
                        )
                    ) { append("JUEGA ") }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF55DFBE),
                            fontSize = 100.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = mogra
                        )
                    ) { append("CON ") }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF34D9A2),
                            fontSize = 100.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = mogra
                        )
                    ) { append("SOLY") }
                }
            )
        }

        Row (modifier = Modifier.absoluteOffset(0.dp, 50.dp)){

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
                    onValueChange = { codigoEstudiante = it },
                    label = { Text("Código estudiantil") },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.End),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.saveEstudiante(nombreEstudiante, codigoEstudiante)
                        navigationViewModel.navigateTo("welcome")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF007BFF)),
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
