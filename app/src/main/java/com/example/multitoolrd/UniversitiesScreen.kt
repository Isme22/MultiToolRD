package com.example.multitoolrd

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

// COLORES DE LA PANTALLA

private val UniversityMint = Color(0xFF95F5BA)
private val UniversityDark = Color(0xFF172554)
private val UniversityMuted = Color(0xFF64748B)
private val UniversityBackground = Color(0xFFF8FAFC)
private val UniversityWhite = Color.White

// MODELO DE DATOS

private data class University(
    val name: String,
    val domain: String,
    val website: String
)

@Composable
fun UniversitiesScreen(
    onBackClick: () -> Unit
) {

    var country by remember {
        mutableStateOf("")
    }

    var universities by remember {
        mutableStateOf<List<University>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var searched by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun searchUniversities() {

        if (country.isBlank()) {
            error = "Introduce el nombre de un país en inglés."
            return
        }

        loading = true
        searched = false
        error = null
        universities = emptyList()

        scope.launch {
            try {
                universities = fetchUniversities(country.trim())
                searched = true
            } catch (e: Exception) {
                error = e.message
                    ?: "No se pudieron consultar las universidades."
            } finally {
                loading = false
            }
        }
    }

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UniversityBackground)
            .padding(20.dp)
    ) {

        // CONTENIDO DESPLAZABLE

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                text = "UNIVERSIDADES DEL MUNDO",
                color = UniversityDark,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Encuentra universidades de diferentes países",
                color = UniversityMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // TARJETA DE BÚSQUEDA

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = UniversityWhite
                ),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Buscar por país",
                        color = UniversityDark,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Escribe el nombre del país en inglés.",
                        color = UniversityMuted,
                        fontSize = 13.sp
                    )

                    Spacer(Modifier.height(18.dp))

                    OutlinedTextField(
                        value = country,
                        onValueChange = {
                            country = it
                            error = null
                            searched = false
                            universities = emptyList()
                        },
                        label = {
                            Text("Nombre del país")
                        },
                        placeholder = {
                            Text("Ejemplo: Dominican Republic")
                        },
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
                            searchUniversities()
                        },
                        enabled = !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UniversityMint,
                            contentColor = UniversityDark
                        )
                    ) {

                        Text(
                            text = if (loading) {
                                "BUSCANDO..."
                            } else {
                                "BUSCAR UNIVERSIDADES"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // INDICADOR DE CARGA

            if (loading) {

                CircularProgressIndicator(
                    color = Color(0xFF16A34A)
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Consultando universidades...",
                    color = UniversityMuted
                )
            }

            // MENSAJES DE ERROR

            error?.let { errorMessage ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE4EC)
                    )
                ) {

                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(18.dp),
                        color = Color(0xFF9F1239)
                    )
                }
            }

            // RESULTADOS

            if (searched) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = UniversityMint.copy(alpha = 0.35f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        Text(
                            text = "RESULTADOS DE LA BÚSQUEDA",
                            color = UniversityDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "${universities.size} universidades encontradas",
                            color = UniversityDark,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                if (universities.isEmpty()) {

                    Text(
                        text = "No se encontraron universidades. " +
                                "Verifica que el país esté escrito en inglés.",
                        color = UniversityMuted,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                } else {

                    universities.forEachIndexed { index, university ->

                        UniversityCard(
                            universityName = university.name,
                            universityDomain = university.domain,
                            universityWebsite = university.website,
                            number = index + 1,
                            onVisitClick = {

                                val website = university.website

                                if (website.isNotBlank()) {

                                    val url = if (
                                        website.startsWith("http://") ||
                                        website.startsWith("https://")
                                    ) {
                                        website
                                    } else {
                                        "https://$website"
                                    }

                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(url)
                                    )

                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        error = "No se pudo abrir el sitio web."
                                    }
                                }
                            }
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }

        // BOTÓN INFERIOR FIJO

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = UniversityDark,
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

// TARJETA DE UNIVERSIDAD

@Composable
private fun UniversityCard(
    universityName: String,
    universityDomain: String,
    universityWebsite: String,
    number: Int,
    onVisitClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = UniversityWhite
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = "UNIVERSIDAD $number",
                color = Color(0xFF15803D),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = universityName,
                color = UniversityDark,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "DOMINIO",
                color = UniversityMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = universityDomain.ifBlank {
                    "No disponible"
                },
                color = UniversityDark,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "PÁGINA WEB",
                color = UniversityMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = universityWebsite.ifBlank {
                    "No disponible"
                },
                color = UniversityDark,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = onVisitClick,
                enabled = universityWebsite.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UniversityMint,
                    contentColor = UniversityDark
                )
            ) {

                Text(
                    text = "VISITAR SITIO WEB",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// CONSUMO DE LA API

private suspend fun fetchUniversities(
    country: String
): List<University> = withContext(Dispatchers.IO) {

    val encodedCountry = URLEncoder.encode(
        country,
        "UTF-8"
    )

    val url = URL(
        "https://adamix.net/proxy.php?country=$encodedCountry"
    )

    val connection = url.openConnection() as HttpURLConnection

    try {

        connection.requestMethod = "GET"
        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        val statusCode = connection.responseCode

        if (statusCode !in 200..299) {
            throw Exception(
                "Error del servidor ($statusCode)."
            )
        }

        val response = connection.inputStream
            .bufferedReader()
            .use {
                it.readText()
            }

        val jsonArray = JSONArray(response)

        val result = mutableListOf<University>()

        for (i in 0 until jsonArray.length()) {

            val item = jsonArray.getJSONObject(i)

            val domains = item.optJSONArray("domains")
            val websites = item.optJSONArray("web_pages")

            val domain = if (
                domains != null && domains.length() > 0
            ) {
                domains.optString(0)
            } else {
                ""
            }

            val website = if (
                websites != null && websites.length() > 0
            ) {
                websites.optString(0)
            } else {
                ""
            }

            result.add(
                University(
                    name = item.optString("name"),
                    domain = domain,
                    website = website
                )
            )
        }

        result

    } finally {
        connection.disconnect()
    }
}