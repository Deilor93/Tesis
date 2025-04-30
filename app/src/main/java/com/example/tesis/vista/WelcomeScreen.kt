package com.example.tesis.vista

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tesis.R

@Composable
fun WelcomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {

    Log.d("DEBUG", "WelcomeScreen ViewModel hash: ${sharedViewModel.hashCode()}")

    val estudiante = sharedViewModel.estudiante
    Log.d("est", sharedViewModel.estudiante.toString())
    Log.d("DEBUG", "Welcome screen got estudiante: ${sharedViewModel.estudiante}")
    val mogra = FontFamily(
        Font(R.font.mogra_regular, FontWeight.Light),
        Font(R.font.mogra_regular, FontWeight.Normal),
        Font(R.font.mogra_regular, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.mogra_regular, FontWeight.Medium),
        Font(R.font.mogra_regular, FontWeight.Bold)
    )

    Row (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFF99)),
    ) {

        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.absoluteOffset(-100.dp, 0.dp)){
            Image(
                painter = painterResource(id = R.drawable.monstruo_2),
                contentDescription = "Soly",
                modifier = Modifier
                    .size(900.dp)
            )
        }

        Column (modifier = Modifier.align(Alignment.CenterVertically).absoluteOffset(-100.dp, 0.dp)){
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF34D9A2),
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = mogra
                        )
                    ) { append("Hola ") }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFFF5500),
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = mogra
                        )
                    ) { append(estudiante?.nombre +" ") }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF34D9A2),
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = mogra
                        )
                    ) { append("Soly quiere jugar contigo") }
                }
            )

            IconButton(
                onClick = {
                    navController.navigate("test")
                },
                modifier = Modifier
                    .align(Alignment.End)
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
