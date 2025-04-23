package com.example.tesis.vista

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tesis.R

@Composable
fun WelcomeScreen(navigationViewModel: NavigationViewModel) {

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
                text = "Soly quiere jugar contigo",
                fontSize = 58.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF34D9A2),
                fontFamily = mogra,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
            )

            Button(
                onClick = {
                    navigationViewModel.navigateTo("test") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF007BFF))
            ) {
                Text("Continuar", color = Color.Black)
            }
        }
    }
}
