package com.example.multitoolrd

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private val GenderPink = Color(0xFFFF56A4)
private val GenderBlue = Color(0xFF80D4FF)
private val GenderPurple = Color(0xFFD48CFF)
private val GenderBackground = Color(0xFFF8FAFC)
private val GenderText = Color(0xFF172554)
private val GenderMuted = Color(0xFF64748B)

private data class GenderResult(
    val name: String,
    val gender: String?,
    val probability: Double
)

@Composable
fun GenderScreen(onBackClick: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<GenderResult?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GenderBackground)
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(22.dp))

            Text(
                text = "PREDICCIÓN DE GÉNERO",
                color = GenderText,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Descubre la predicción estadística asociada a un nombre",
                color = GenderMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "¿Qué nombre deseas consultar?",
                        color = GenderText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            result = null
                            error = null
                        },
                        label = { Text("Nombre") },
                        placeholder = { Text("Ejemplo: María") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                error = "Introduce un nombre para continuar."
                            } else {
                                loading = true
                                error = null
                                result = null

                                scope.launch {
                                    try {
                                        result = fetchGender(name.trim())
                                    } catch (e: Exception) {
                                        error = e.message
                                            ?: "No se pudo realizar la consulta."
                                    } finally {
                                        loading = false
                                    }
                                }
                            }
                        },
                        enabled = !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GenderPurple,
                            contentColor = GenderText
                        )
                    ) {
                        Text(
                            text = if (loading) {
                                "CONSULTANDO..."
                            } else {
                                "CONSULTAR GÉNERO"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            if (loading) {
                CircularProgressIndicator(
                    color = GenderPink
                )
            }

            error?.let { errorMessage ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE4EC)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(18.dp),
                        color = Color(0xFF9F1239)
                    )
                }
            }

            result?.let { genderResult ->

                val isMale = genderResult.gender == "male"
                val isFemale = genderResult.gender == "female"

                val resultColor = when {
                    isMale -> GenderBlue
                    isFemale -> GenderPink
                    else -> Color(0xFFE5E7EB)
                }

                val genderLabel = when {
                    isMale -> "Masculino"
                    isFemale -> "Femenino"
                    else -> "Sin predicción disponible"
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = resultColor.copy(
                            alpha = if (isFemale) 0.25f else 0.45f
                        )
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "RESULTADO",
                            color = GenderText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = genderResult.name,
                            color = GenderText,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = genderLabel,
                            color = GenderText,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(16.dp))

                        if (genderResult.gender != null) {
                            val percentage =
                                (genderResult.probability * 100).toInt()

                            Text(
                                text = "$percentage% de probabilidad estimada",
                                color = GenderText,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(12.dp))

                            LinearProgressIndicator(
                                progress = {
                                    genderResult.probability.toFloat()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(9.dp),
                                color = if (isMale) {
                                    Color(0xFF0284C7)
                                } else {
                                    GenderPink
                                },
                                trackColor = Color.White
                            )
                        } else {
                            Text(
                                text = "No hay suficientes datos para este nombre.",
                                color = GenderMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "La predicción se basa en estadísticas " +
                            "de nombres y no determina la identidad " +
                            "de género de una persona.",
                    color = GenderMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.height(20.dp))
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GenderText,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "← VOLVER AL MENÚ",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private suspend fun fetchGender(name: String): GenderResult =
    withContext(Dispatchers.IO) {

        val encodedName = URLEncoder.encode(name, "UTF-8")

        val connection = URL(
            "https://api.genderize.io/?name=$encodedName"
        ).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val statusCode = connection.responseCode

            if (statusCode !in 200..299) {
                throw Exception(
                    "Error del servicio ($statusCode). Intenta más tarde."
                )
            }

            val response = connection.inputStream
                .bufferedReader()
                .use { it.readText() }

            val json = JSONObject(response)

            GenderResult(
                name = json.optString("name", name),
                gender = if (json.isNull("gender")) {
                    null
                } else {
                    json.optString("gender")
                },
                probability = json.optDouble("probability", 0.0)
            )

        } finally {
            connection.disconnect()
        }
    }