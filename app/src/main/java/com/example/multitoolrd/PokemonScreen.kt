package com.example.multitoolrd

import android.media.MediaPlayer
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
import java.net.URLEncoder
import java.util.Locale

// COLORES MULTITOOL RD

private val PokemonYellow = Color(0xFFFCE93A)
private val PokemonPurple = Color(0xFFD48CFF)
private val PokemonMint = Color(0xFF95F5BA)
private val PokemonBackground = Color(0xFFF8FAFC)
private val PokemonDark = Color(0xFF172554)
private val PokemonMuted = Color(0xFF64748B)

// MODELO DE DATOS

private data class PokemonData(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val baseExperience: Int,
    val abilities: List<String>,
    val soundUrl: String?
)

// PANTALLA PRINCIPAL

@Composable
fun PokemonScreen(
    onBackClick: () -> Unit
) {

    var pokemonName by remember {
        mutableStateOf("")
    }

    var pokemon by remember {
        mutableStateOf<PokemonData?>(null)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    var playing by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    var mediaPlayer by remember {
        mutableStateOf<MediaPlayer?>(null)
    }

    fun stopSound() {
        mediaPlayer?.release()
        mediaPlayer = null
        playing = false
    }

    fun searchPokemon() {

        if (pokemonName.isBlank()) {
            error = "Introduce el nombre de un Pokémon."
            return
        }

        stopSound()
        loading = true
        error = null
        pokemon = null

        scope.launch {
            try {
                pokemon = fetchPokemon(
                    pokemonName.trim().lowercase(Locale.ROOT)
                )
            } catch (e: Exception) {
                error = e.message
                    ?: "No se pudo consultar el Pokémon."
            } finally {
                loading = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopSound()
        }
    }

    BackHandler {
        stopSound()
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PokemonBackground)
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
                text = "MUNDO POKÉMON",
                color = PokemonDark,
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Descubre sus habilidades, experiencia y sonido",
                color = PokemonMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(26.dp))

            // TARJETA DE BÚSQUEDA

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
                        text = "Buscar Pokémon",
                        color = PokemonDark,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Escribe su nombre en inglés.",
                        color = PokemonMuted,
                        fontSize = 13.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = pokemonName,
                        onValueChange = {
                            pokemonName = it
                            error = null
                        },
                        label = {
                            Text("Nombre del Pokémon")
                        },
                        placeholder = {
                            Text("Ejemplo: pikachu")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(Modifier.height(18.dp))

                    Button(
                        onClick = {
                            searchPokemon()
                        },
                        enabled = !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PokemonYellow,
                            contentColor = PokemonDark
                        )
                    ) {

                        Text(
                            text = if (loading) {
                                "BUSCANDO..."
                            } else {
                                "BUSCAR POKÉMON"
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
                    color = PokemonPurple
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Consultando PokéAPI...",
                    color = PokemonMuted
                )
            }

            // ERRORES

            error?.let { message ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE4EC)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Text(
                        text = message,
                        modifier = Modifier.padding(18.dp),
                        color = Color(0xFF9F1239)
                    )
                }
            }

            // RESULTADO

            pokemon?.let { data ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // ENCABEZADO CON DEGRADADO

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            PokemonYellow.copy(alpha = 0.65f),
                                            PokemonPurple.copy(alpha = 0.35f)
                                        )
                                    )
                                )
                                .padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "POKÉMON #${data.id}",
                                color = PokemonDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(Modifier.height(12.dp))

                            AsyncImage(
                                model = data.imageUrl,
                                contentDescription = "Imagen de ${data.name}",
                                modifier = Modifier.size(190.dp),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = data.name.replaceFirstChar {
                                    it.uppercase()
                                },
                                color = PokemonDark,
                                fontSize = 29.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        // INFORMACIÓN DEL POKÉMON

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp)
                        ) {

                            Text(
                                text = "EXPERIENCIA BASE",
                                color = PokemonMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "${data.baseExperience} XP",
                                color = PokemonDark,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(Modifier.height(20.dp))

                            HorizontalDivider(
                                color = Color(0xFFE2E8F0)
                            )

                            Spacer(Modifier.height(20.dp))

                            Text(
                                text = "HABILIDADES",
                                color = PokemonMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(14.dp))

                            data.abilities.forEach { ability ->

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = PokemonMint.copy(
                                            alpha = 0.45f
                                        )
                                    )
                                ) {

                                    Text(
                                        text = ability.replace("-", " ")
                                            .replaceFirstChar {
                                                it.uppercase()
                                            },
                                        modifier = Modifier.padding(14.dp),
                                        color = PokemonDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(Modifier.height(18.dp))

                            HorizontalDivider(
                                color = Color(0xFFE2E8F0)
                            )

                            Spacer(Modifier.height(20.dp))

                            Text(
                                text = "SONIDO DEL POKÉMON",
                                color = PokemonMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(14.dp))

                            if (data.soundUrl != null) {

                                Button(
                                    onClick = {

                                        if (playing) {
                                            stopSound()
                                        } else {

                                            try {
                                                stopSound()

                                                val player = MediaPlayer()

                                                mediaPlayer = player
                                                playing = true

                                                player.setDataSource(
                                                    data.soundUrl
                                                )

                                                player.setOnPreparedListener {
                                                    it.start()
                                                }

                                                player.setOnCompletionListener {
                                                    stopSound()
                                                }

                                                player.setOnErrorListener { _, _, _ ->
                                                    stopSound()
                                                    error = "No se pudo reproducir el sonido."
                                                    true
                                                }

                                                player.prepareAsync()

                                            } catch (e: Exception) {
                                                stopSound()
                                                error = "Error al reproducir el sonido."
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PokemonPurple,
                                        contentColor = PokemonDark
                                    )
                                ) {

                                    Text(
                                        text = if (playing) {
                                            "DETENER SONIDO"
                                        } else {
                                            "REPRODUCIR SONIDO"
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            } else {

                                Text(
                                    text = "Este Pokémon no tiene un sonido disponible.",
                                    color = PokemonMuted,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }

        // BOTÓN INFERIOR FIJO

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                stopSound()
                onBackClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PokemonDark,
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

// CONSULTA A POKÉAPI

private suspend fun fetchPokemon(
    name: String
): PokemonData = withContext(Dispatchers.IO) {

    val encodedName = URLEncoder.encode(
        name,
        "UTF-8"
    )

    val connection = URL(
        "https://pokeapi.co/api/v2/pokemon/$encodedName"
    ).openConnection() as HttpURLConnection

    try {

        connection.requestMethod = "GET"
        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        val statusCode = connection.responseCode

        if (statusCode == 404) {
            throw Exception(
                "No se encontró ese Pokémon. Verifica el nombre."
            )
        }

        if (statusCode !in 200..299) {
            throw Exception(
                "Error al consultar PokéAPI ($statusCode)."
            )
        }

        val response = connection.inputStream
            .bufferedReader()
            .use { it.readText() }

        val json = JSONObject(response)

        // IMAGEN OFICIAL

        val sprites = json.getJSONObject("sprites")

        val officialArtwork = sprites
            .optJSONObject("other")
            ?.optJSONObject("official-artwork")
            ?.optString("front_default", "")
            .orEmpty()

        val imageUrl = officialArtwork.ifBlank {
            sprites.optString("front_default", "")
        }

        // HABILIDADES

        val abilitiesArray = json.getJSONArray("abilities")

        val abilities = mutableListOf<String>()

        for (i in 0 until abilitiesArray.length()) {

            val ability = abilitiesArray
                .getJSONObject(i)
                .getJSONObject("ability")
                .getString("name")

            abilities.add(ability)
        }

        // SONIDO MÁS RECIENTE

        val cries = json.optJSONObject("cries")

        val soundUrl = cries
            ?.optString("latest", "")
            ?.takeIf { it.isNotBlank() && it != "null" }

        PokemonData(
            id = json.getInt("id"),
            name = json.getString("name"),
            imageUrl = imageUrl,
            baseExperience = json.optInt("base_experience", 0),
            abilities = abilities,
            soundUrl = soundUrl
        )

    } finally {
        connection.disconnect()
    }
}