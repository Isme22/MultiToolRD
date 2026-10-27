package com.example.multitoolrd

import android.content.Intent
import android.net.Uri
import android.text.Html
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

// ============================================
// PALETA DE COLORES MULTITOOL RD
// ============================================

private val NewsTurquoise = Color(0xFF62FBFF)
private val NewsPurple = Color(0xFFD48CFF)
private val NewsBackground = Color(0xFFF8FAFC)
private val NewsDark = Color(0xFF172554)
private val NewsMuted = Color(0xFF64748B)

// ============================================
// API REST DEL BLOG DE KINSTA
// ============================================

private const val NEWS_API =
    "https://kinsta.com/wp-json/wp/v2/posts?per_page=3&orderby=date&order=desc"

// LOGO OFICIAL DE KINSTA

private const val KINSTA_LOGO =
    "https://kinsta.com/wp-content/uploads/2023/12/kinsta-logo.jpeg"

// ============================================
// MODELO DE NOTICIA
// ============================================

private data class NewsArticle(
    val title: String,
    val summary: String,
    val link: String,
    val date: String
)

// ============================================
// PANTALLA PRINCIPAL DE NOTICIAS
// ============================================

@Composable
fun NewsScreen(onBackClick: () -> Unit) {

    var articles by remember {
        mutableStateOf<List<NewsArticle>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // FUNCIÓN PARA CARGAR NOTICIAS

    fun loadNews() {

        loading = true
        error = null

        scope.launch {

            try {

                articles = fetchNews()

            } catch (e: Exception) {

                articles = emptyList()

                error = e.message
                    ?: "No se pudieron obtener las noticias."

            } finally {

                loading = false
            }
        }
    }

    // CARGAR AUTOMÁTICAMENTE AL ENTRAR

    LaunchedEffect(Unit) {
        loadNews()
    }

    // BOTÓN ATRÁS DEL TELÉFONO

    BackHandler {
        onBackClick()
    }

    // ========================================
    // CONTENIDO DE LA PANTALLA
    // ========================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NewsBackground)
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

            // TÍTULO PRINCIPAL

            Text(
                text = "NOTICIAS WORDPRESS",
                color = NewsDark,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Las últimas publicaciones del blog de Kinsta",
                color = NewsMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(25.dp))

            // ====================================
            // TARJETA CON LOGO OFICIAL DE KINSTA
            // ====================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NewsTurquoise.copy(alpha = 0.30f)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // LOGO REAL DE LA PÁGINA WEB

                    Card(
                        modifier = Modifier.size(76.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            AsyncImage(
                                model = KINSTA_LOGO,
                                contentDescription = "Logo oficial de Kinsta",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Kinsta Blog",
                            color = NewsDark,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = "Noticias de tecnología y desarrollo web",
                            color = NewsMuted,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = "Sitio desarrollado con WordPress",
                            color = Color(0xFF087F8C),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ====================================
            // TÍTULO DE PUBLICACIONES
            // ====================================

            Text(
                text = "ÚLTIMAS 3 PUBLICACIONES",
                modifier = Modifier.fillMaxWidth(),
                color = NewsDark,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            // ====================================
            // ESTADO DE CARGA
            // ====================================

            if (loading) {

                CircularProgressIndicator(
                    color = NewsPurple
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Consultando las noticias de Kinsta...",
                    color = NewsMuted
                )
            }

            // ====================================
            // MOSTRAR ERRORES
            // ====================================

            error?.let { message ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
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
            }

            // ====================================
            // MOSTRAR LAS 3 NOTICIAS
            // ====================================

            if (!loading && error == null) {

                articles.forEachIndexed { index, article ->

                    NewsArticleCard(
                        article = article,
                        number = index + 1,
                        onVisitClick = {

                            try {

                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(article.link)
                                )

                                context.startActivity(intent)

                            } catch (e: Exception) {

                                error = "No se pudo abrir la noticia."
                            }
                        }
                    )

                    Spacer(Modifier.height(14.dp))
                }

                if (articles.isEmpty()) {

                    Text(
                        text = "No hay publicaciones disponibles.",
                        color = NewsMuted
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ====================================
            // BOTÓN ACTUALIZAR
            // ====================================

            Button(
                onClick = { loadNews() },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NewsTurquoise,
                    contentColor = NewsDark
                )
            ) {

                Text(
                    text = "ACTUALIZAR NOTICIAS",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(20.dp))
        }

        Spacer(Modifier.height(12.dp))

        // ====================================
        // BOTÓN INFERIOR PARA VOLVER
        // ====================================

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NewsDark,
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

// ============================================
// TARJETA INDIVIDUAL DE NOTICIA
// ============================================

@Composable
private fun NewsArticleCard(
    article: NewsArticle,
    number: Int,
    onVisitClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            // NÚMERO DE NOTICIA

            Text(
                text = "NOTICIA $number",
                color = Color(0xFF087F8C),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(12.dp))

            // TITULAR

            Text(
                text = article.title,
                color = NewsDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 26.sp
            )

            Spacer(Modifier.height(12.dp))

            // FECHA

            Text(
                text = article.date,
                color = NewsMuted,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(
                color = Color(0xFFE2E8F0)
            )

            Spacer(Modifier.height(16.dp))

            // RESUMEN

            Text(
                text = article.summary,
                color = NewsMuted,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(20.dp))

            // BOTÓN VISITAR NOTICIA

            Button(
                onClick = onVisitClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NewsTurquoise,
                    contentColor = NewsDark
                )
            ) {

                Text(
                    text = "VISITAR NOTICIA",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ============================================
// LIMPIAR ETIQUETAS HTML DE WORDPRESS
// ============================================

private fun cleanHtml(html: String): String {

    return Html.fromHtml(
        html,
        Html.FROM_HTML_MODE_COMPACT
    ).toString().trim()
}

// ============================================
// CONSULTAR API REST DE KINSTA
// ============================================

private suspend fun fetchNews(): List<NewsArticle> =
    withContext(Dispatchers.IO) {

        val connection =
            URL(NEWS_API).openConnection() as HttpURLConnection

        try {

            connection.requestMethod = "GET"

            connection.connectTimeout = 15000

            connection.readTimeout = 15000

            connection.setRequestProperty(
                "Accept",
                "application/json"
            )

            if (connection.responseCode !in 200..299) {

                throw Exception(
                    "Error del servidor (${connection.responseCode})."
                )
            }

            val response = connection.inputStream
                .bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONArray(response)

            val result = mutableListOf<NewsArticle>()

            // OBTENER SOLO 3 PUBLICACIONES

            for (i in 0 until minOf(3, jsonArray.length())) {

                val item = jsonArray.getJSONObject(i)

                // TITULAR

                val title = cleanHtml(
                    item.getJSONObject("title")
                        .optString("rendered", "")
                )

                // RESUMEN

                val summary = cleanHtml(
                    item.getJSONObject("excerpt")
                        .optString("rendered", "")
                )

                // FECHA

                val date = item
                    .optString("date", "")
                    .substringBefore("T")

                // ENLACE ORIGINAL

                val link = item.optString("link", "")

                result.add(
                    NewsArticle(
                        title = title,
                        summary = summary,
                        link = link,
                        date = date
                    )
                )
            }

            result

        } finally {

            connection.disconnect()
        }
    }