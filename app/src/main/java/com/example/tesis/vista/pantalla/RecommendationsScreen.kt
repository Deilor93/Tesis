package com.example.tesis.vista.pantalla

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tesis.logica.Clasificacion
import com.example.tesis.logica.DbClass
import com.example.tesis.vista.componente.StarRating
import com.example.tesis.vista.modelo.ClasificacionConRecomendacion
import com.example.tesis.vista.util.Mogra
import com.example.tesis.vista.viewmodel.SharedViewModel

@Composable
fun RecommendationsScreen(
    navController: NavHostController,
    context: Context,
    sharedViewModel: SharedViewModel
){

    val db = remember { DbClass.getDatabase(context) }
    val clasificacionDao = db.clasificacionDao()
    val recomendacionDao = db.recomendacionDao()

    //Mapa de etiqueta con resultado de respuestas correctas
    val intelligencias = mapOf(
        "Inteligencia Lingüística" to sharedViewModel.contLing,
        "Inteligencia Lógico-Matemática" to sharedViewModel.contMat,
        "Inteligencia Espacial" to sharedViewModel.contEsp,
        "Inteligencia Musical" to sharedViewModel.contMus,
        "Inteligencia Corporal-Cinestésica" to sharedViewModel.contCor,
        "Inteligencia Interpersonal" to sharedViewModel.contInter,
        "Inteligencia Intrapersonal" to sharedViewModel.contIntra,
        "Inteligencia Naturalista" to sharedViewModel.contNatu
    )

    val emojiMapInteligencia = mapOf(
        "Inteligencia Lingüística" to "📚",
        "Inteligencia Lógico-Matemática" to "🧠",
        "Inteligencia Espacial" to "📐",
        "Inteligencia Musical" to "🎸",
        "Inteligencia Corporal-Cinestésica" to "🏃",
        "Inteligencia Interpersonal" to "🫂",
        "Inteligencia Intrapersonal" to "🧘",
        "Inteligencia Naturalista" to "🌿"
    )

    val colorMapInteligencia = mapOf(
        "Inteligencia Lingüística" to Color(0xFF475CD7),       // Azul
        "Inteligencia Lógico-Matemática" to Color(0xFF7E8A7E), // Gris
        "Inteligencia Espacial" to Color(0xFFCE964B),          // Naranja
        "Inteligencia Musical" to Color(0xFFBA33D2),           // Morado
        "Inteligencia Corporal-Cinestésica" to Color(0xFFFA5F5F), // Rojo
        "Inteligencia Interpersonal" to Color(0xFF58EEDE),     // Aguamarina
        "Inteligencia Intrapersonal" to Color(0xFFBB8C7A),     // Cafe
        "Inteligencia Naturalista" to Color(0xFF99D552)        // Verde
    )

    val potLing = sharedViewModel.potLing
    val potMus = sharedViewModel.potMus
    val potIntra = sharedViewModel.potIntra

    val resultados = remember {
        mutableStateOf<List<ClasificacionConRecomendacion>>(emptyList())
    }


    //Calculo para asignacion de categoria dependiendo de la cantidad de respuestas correctas
    fun calcClas(puntuacion: Int): Int = when {
        puntuacion <= 2 -> 1
        puntuacion == 3 -> 2
        else -> 3
    }

    /**Selecciona las recomendaciones a mostrar por cada inteligencia basado en el resultado
     * obtenido en cada una de ellas y las guarda en un data class con la informacion que se
     * va a mostrar en pantalla*/
    LaunchedEffect(Unit) {
        val result = intelligencias.map { (nombreInteligencia, puntuacion) ->
            val clasId = calcClas(puntuacion)
            val clasificacion = clasificacionDao.getClasificacionById(clasId)
            val catId = when (nombreInteligencia) {
                "Inteligencia Lingüística" -> 1
                "Inteligencia Lógico-Matemática" -> 2
                "Inteligencia Espacial" -> 3
                "Inteligencia Musical" -> 4
                "Inteligencia Corporal-Cinestésica" -> 5
                "Inteligencia Interpersonal" -> 6
                "Inteligencia Intrapersonal" -> 7
                "Inteligencia Naturalista" -> 8
                else -> 0 // default or error case, depending on your design
            }
            Log.d("catId", catId.toString())
            val recomendacion = clasificacion?.let {
                recomendacionDao.getRecomendacionByClasificacionYCategoria(
                    clasificacionId = it.id,
                    categoriaId = catId
                )
            }
            //Data class con la informacion que se va a mostrar en pantalla
            ClasificacionConRecomendacion(nombreInteligencia, puntuacion, clasificacion, recomendacion)

        }
        resultados.value = result
    }
    //Log.d("Results", resultados.toString())

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFFFFF99)
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            //Escribe en pantalla si el niño tiene potencial de desarrollo de ciertas inteligencias en casa
            if (potLing == 1 || potMus == 1 || potIntra == 1) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 40.sp,
                                fontFamily = Mogra,
                                color = Color(0xFF646262)
                            )
                        ){
                            append(text = "El estudiante tiene en casa herramientas para ayudar " +
                                    "a potenciar las siguientes inteligencias")
                        }
                    },
                    modifier = Modifier.padding(bottom = 25.dp)
                )
                if (potLing == 1){
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = ParagraphStyle(
                                    textIndent = TextIndent(firstLine = 35.sp)
                                )
                            ){
                                append(text = "•Inteligencia Lingüística")
                            }
                        },
                        modifier = Modifier.padding(bottom = 25.dp),
                        color = Color(0xFF475CD7),
                        style = TextStyle(fontSize = 20.sp),
                        fontFamily = Mogra
                    )
                }
                if (potMus == 1){
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = ParagraphStyle(
                                    textIndent = TextIndent(firstLine = 35.sp)
                                )
                            ){
                                append(text = "•Inteligencia Musical")
                            }
                        },
                        modifier = Modifier.padding(bottom = 25.dp),
                        color = Color(0xFFBA33D2),
                        style = TextStyle(fontSize = 20.sp),
                        fontFamily = Mogra
                    )
                }
                if (potIntra == 1){
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = ParagraphStyle(
                                    textIndent = TextIndent(firstLine = 35.sp)
                                )
                            ){
                                append(text = "•Inteligencia Intrapersonal")
                            }
                        },
                        modifier = Modifier.padding(bottom = 25.dp),
                        color = Color(0xFFBB8C7A),
                        style = TextStyle(fontSize = 20.sp),
                        fontFamily = Mogra
                    )
                }
            }

            HorizontalDivider(thickness = 4.dp)

            //Escribe los resultados en pantalla
            resultados.value.forEach { item ->
                val color = colorMapInteligencia[item.nombre] ?: Color.Black
                Column(modifier = Modifier.padding(vertical = 15.dp)) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Mogra,
                                    color = color
                                )
                            ){
                                val emoji = emojiMapInteligencia[item.nombre] ?: ""
                                append(text = "• ${item.nombre} $emoji:")
                            }
                        },
                        fontSize = 30.sp,
                        fontFamily = Mogra
                    )

                    StarRating(rating = item.puntuacion)

                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = ParagraphStyle(
                                    textIndent = TextIndent(firstLine = 35.sp)
                                )
                            ) {
                                append(item.recomendacion?.recomendacion ?: "Recomendación no disponible")
                            }
                        },
                        modifier = Modifier.padding(bottom = 25.dp),
                        style = TextStyle(fontSize = 20.sp),
                        fontFamily = Mogra,
                        color = color
                    )
                }
                HorizontalDivider(thickness = 2.dp)
            }
        }
    }
}