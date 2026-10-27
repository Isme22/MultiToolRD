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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

// PALETA MULTITOOL RD

private val WeatherBlue = Color(0xFF80D4FF)
private val WeatherTurquoise = Color(0xFF62FBFF)
private val WeatherPurple = Color(0xFFD48CFF)
private val WeatherMint = Color(0xFF95F5BA)
private val WeatherBackground = Color(0xFFF8FAFC)
private val WeatherDark = Color(0xFF172554)
private val WeatherMuted = Color(0xFF64748B)

// MODELO DE DATOS

private data class WeatherData(
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherCode: Int,
    val isDay: Boolean,
    val maxTemperature: Double,
    val minTemperature: Double,
    val rainProbability: Int?,
    val date: String,
    val observationTime: String
)

// DESCRIPCIÓN DEL CLIMA

private fun weatherDescription(code: Int): String {
    return when (code) {
        0 -> "Cielo despejado"
        1 -> "Mayormente despejado"
        2 -> "Parcialmente nublado"
        3 -> "Nublado"
        45, 48 -> "Niebla"
        51, 53, 55 -> "Llovizna"
        56, 57 -> "Llovizna helada"
        61, 63, 65 -> "Lluvia"
        66, 67 -> "Lluvia helada"
        71, 73, 75, 77 -> "Nieve"
        80, 81, 82 -> "Chubascos"
        85, 86 -> "Chubascos de nieve"
        95, 96, 99 -> "Tormenta eléctrica"
        else -> "Condiciones variables"
    }
}

// PANTALLA PRINCIPAL DEL CLIMA

@Composable
fun WeatherScreen(
    onBackClick: () -> Unit
) {

    var weather by remember {
        mutableStateOf<WeatherData?>(null)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()

    fun loadWeather() {
        loading = true
        error = null

        scope.launch {
            try {
                weather = fetchWeather()
            } catch (e: Exception) {
                error = e.message
                    ?: "No se pudo obtener el clima."
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadWeather()
    }

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherBackground)
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                text = "CLIMA EN RD",
                color = WeatherDark,
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Consulta las condiciones meteorológicas de hoy",
                color = WeatherMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))

            if (loading) {
                CircularProgressIndicator(
                    color = WeatherBlue
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Consultando el clima...",
                    color = WeatherMuted
                )
            }

            error?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE4EC)
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(18.dp),
                        color = Color(0xFF9F1239)
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            weather?.let { data ->

                // TARJETA PRINCIPAL

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        WeatherBlue,
                                        WeatherTurquoise.copy(
                                            alpha = 0.65f
                                        )
                                    )
                                )
                            )
                            .padding(26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "SANTO DOMINGO",
                            color = WeatherDark,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "República Dominicana",
                            color = WeatherDark.copy(alpha = 0.75f),
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.height(20.dp))

                        // SÍMBOLO METEOROLÓGICO

                        Text(
                            text = when (data.weatherCode) {
                                0, 1 -> if (data.isDay) "☀" else "☾"
                                2, 3 -> "☁"
                                45, 48 -> "≋"
                                95, 96, 99 -> "ϟ"
                                else -> "☂"
                            },
                            color = WeatherDark,
                            fontSize = 65.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "${data.temperature}°C",
                            color = WeatherDark,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = weatherDescription(data.weatherCode),
                            color = WeatherDark,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "Sensación térmica: ${data.feelsLike}°C",
                            color = WeatherDark,
                            fontSize = 15.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = data.date,
                            color = WeatherDark.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = "Actualización: ${data.observationTime}",
                            color = WeatherDark.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "DETALLES DEL CLIMA",
                    modifier = Modifier.fillMaxWidth(),
                    color = WeatherDark,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                // FILA 1

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    WeatherDetailCard(
                        title = "HUMEDAD",
                        value = "${data.humidity}%",
                        color = WeatherMint,
                        modifier = Modifier.weight(1f)
                    )

                    WeatherDetailCard(
                        title = "VIENTO",
                        value = "${data.windSpeed} km/h",
                        color = WeatherPurple,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // FILA 2

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    WeatherDetailCard(
                        title = "MÁXIMA HOY",
                        value = "${data.maxTemperature}°C",
                        color = WeatherBlue,
                        modifier = Modifier.weight(1f)
                    )

                    WeatherDetailCard(
                        title = "MÍNIMA HOY",
                        value = "${data.minTemperature}°C",
                        color = WeatherTurquoise,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                WeatherDetailCard(
                    title = "PROBABILIDAD MÁXIMA DE LLUVIA HOY",
                    value = data.rainProbability?.let {
                        "$it%"
                    } ?: "No disponible",
                    color = WeatherBlue,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Datos meteorológicos proporcionados " +
                            "por Open-Meteo.",
                    color = WeatherMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(24.dp))

            // ACTUALIZAR CLIMA

            Button(
                onClick = {
                    loadWeather()
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WeatherBlue,
                    contentColor = WeatherDark
                )
            ) {

                Text(
                    text = "ACTUALIZAR CLIMA",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(20.dp))
        }

        // VOLVER AL MENÚ

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WeatherDark,
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

// TARJETA DE INFORMACIÓN

@Composable
private fun WeatherDetailCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.35f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            Text(
                text = title,
                color = WeatherMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = value,
                color = WeatherDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

// CONSUMO DE OPEN-METEO

private suspend fun fetchWeather(): WeatherData =
    withContext(Dispatchers.IO) {

        // COORDENADAS DE SANTO DOMINGO
        val latitude = 18.4861
        val longitude = -69.9312

        val apiUrl =
            "https://api.open-meteo.com/v1/forecast" +
                    "?latitude=$latitude" +
                    "&longitude=$longitude" +
                    "&current=temperature_2m,relative_humidity_2m," +
                    "apparent_temperature,is_day,weather_code,wind_speed_10m" +
                    "&daily=temperature_2m_max,temperature_2m_min," +
                    "precipitation_probability_max" +
                    "&timezone=America%2FSanto_Domingo" +
                    "&forecast_days=1"

        val connection =
            URL(apiUrl).openConnection() as HttpURLConnection

        try {

            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            if (connection.responseCode !in 200..299) {
                throw Exception(
                    "Error al consultar el servicio meteorológico."
                )
            }

            val response = connection.inputStream
                .bufferedReader()
                .use { it.readText() }

            val json = JSONObject(response)

            val current = json.getJSONObject("current")
            val daily = json.getJSONObject("daily")

            val rawDate = daily
                .getJSONArray("time")
                .getString(0)

            val dateFormatter = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )

            val displayFormatter = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            )

            val formattedDate = dateFormatter.parse(rawDate)?.let {
                displayFormatter.format(it)
            } ?: rawDate

            val rainArray = daily.optJSONArray(
                "precipitation_probability_max"
            )

            val rainProbability = if (
                rainArray != null &&
                rainArray.length() > 0 &&
                !rainArray.isNull(0)
            ) {
                rainArray.getInt(0)
            } else {
                null
            }

            WeatherData(
                temperature = current.getDouble("temperature_2m"),
                feelsLike = current.getDouble("apparent_temperature"),
                humidity = current.getInt("relative_humidity_2m"),
                windSpeed = current.getDouble("wind_speed_10m"),
                weatherCode = current.getInt("weather_code"),
                isDay = current.getInt("is_day") == 1,
                maxTemperature = daily
                    .getJSONArray("temperature_2m_max")
                    .getDouble(0),
                minTemperature = daily
                    .getJSONArray("temperature_2m_min")
                    .getDouble(0),
                rainProbability = rainProbability,
                date = formattedDate,
                observationTime = current
                    .getString("time")
                    .substringAfter("T")
            )

        } finally {
            connection.disconnect()
        }
    }