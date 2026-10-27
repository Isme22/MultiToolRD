package com.example.multitoolrd

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val RandomPurple = Color(0xFFD48CFF)
private val RandomBackground = Color(0xFFF8FAFC)
private val RandomDark = Color(0xFF172554)
private val RandomMuted = Color(0xFF64748B)

private data class RandomPerson(
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val age: Int,
    val photoUrl: String
)

@Composable
fun RandomUserScreen(onBackClick: () -> Unit) {

    var person by remember { mutableStateOf<RandomPerson?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    fun loadPerson() {
        loading = true
        error = null

        scope.launch {
            try {
                person = fetchRandomPerson()
            } catch (e: Exception) {
                error = "No se pudo obtener la persona. Verifica internet."
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadPerson()
    }

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RandomBackground)
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
                text = "PERSONA ALEATORIA",
                color = RandomDark,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Conoce personas de diferentes partes del mundo",
                color = RandomMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(30.dp))

            if (loading) {
                CircularProgressIndicator(color = RandomPurple)
                Spacer(Modifier.height(16.dp))
                Text("Buscando una persona...")
            }

            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            person?.let { user ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = user.photoUrl,
                            contentDescription = "Fotografía de la persona",
                            modifier = Modifier
                                .size(145.dp)
                                .clip(CircleShape)
                                .background(
                                    RandomPurple.copy(alpha = 0.25f)
                                ),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = "${user.firstName} ${user.lastName}",
                            color = RandomDark,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(24.dp))

                        PersonDetail(
                            label = "NOMBRE",
                            value = user.firstName
                        )

                        PersonDetail(
                            label = "APELLIDO",
                            value = user.lastName
                        )

                        PersonDetail(
                            label = "FECHA DE NACIMIENTO",
                            value = user.birthDate
                        )

                        PersonDetail(
                            label = "EDAD",
                            value = "${user.age} años"
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { loadPerson() },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RandomPurple,
                    contentColor = RandomDark
                )
            ) {
                Text(
                    text = "GENERAR OTRA PERSONA",
                    fontWeight = FontWeight.Bold
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
                containerColor = RandomDark,
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

@Composable
private fun PersonDetail(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp)
    ) {
        Text(
            text = label,
            color = RandomMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = value,
            color = RandomDark,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp),
            color = Color(0xFFE2E8F0)
        )
    }
}

private suspend fun fetchRandomPerson(): RandomPerson =
    withContext(Dispatchers.IO) {

        val connection = URL(
            "https://randomuser.me/api/?results=1"
        ).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            if (connection.responseCode !in 200..299) {
                throw Exception("Error al consultar la API")
            }

            val response = connection.inputStream
                .bufferedReader()
                .use { it.readText() }

            val json = JSONObject(response)
                .getJSONArray("results")
                .getJSONObject(0)

            val name = json.getJSONObject("name")
            val dob = json.getJSONObject("dob")
            val picture = json.getJSONObject("picture")

            val rawDate = dob.getString("date")

            val inputFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.US
            ).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            val outputFormat = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            )

            val formattedDate = inputFormat.parse(rawDate)?.let {
                outputFormat.format(it)
            } ?: rawDate.substringBefore("T")

            RandomPerson(
                firstName = name.getString("first"),
                lastName = name.getString("last"),
                birthDate = formattedDate,
                age = dob.getInt("age"),
                photoUrl = picture.getString("large")
            )

        } finally {
            connection.disconnect()
        }
    }