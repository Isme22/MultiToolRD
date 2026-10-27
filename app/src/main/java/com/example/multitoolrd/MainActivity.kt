package com.example.multitoolrd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================
// PALETA OFICIAL MULTITOOL RD
// ============================================

private val Pink = Color(0xFFFF56A4)
private val Yellow = Color(0xFFFCE93A)
private val Mint = Color(0xFF95F5BA)
private val SkyBlue = Color(0xFF80D4FF)
private val Purple = Color(0xFFD48CFF)
private val Turquoise = Color(0xFF62FBFF)

private val Background = Color(0xFFF8FAFC)
private val DarkText = Color(0xFF172554)
private val MutedText = Color(0xFF64748B)

// Fondos pastel para las tarjetas

private val PinkBackground = Color(0xFFFFE1EF)
private val PurpleBackground = Color(0xFFF1E4FF)
private val MintBackground = Color(0xFFDDF9E8)
private val BlueBackground = Color(0xFFE0F3FF)
private val YellowBackground = Color(0xFFFFF5C7)
private val TurquoiseBackground = Color(0xFFDAFAFC)

// ============================================
// MAIN ACTIVITY
// ============================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MultiToolApp()
            }
        }
    }
}

// ============================================
// NAVEGACIÓN PRINCIPAL
// ============================================

@Composable
fun MultiToolApp() {

    var currentScreen by remember {
        mutableStateOf("home")
    }

    if (currentScreen != "home") {
        BackHandler {
            currentScreen = "home"
        }
    }

    when (currentScreen) {

        "home" -> HomeScreen(
            onNavigate = { screen ->
                currentScreen = screen
            }
        )

        "gender" -> GenderScreen(
            onBackClick = {
                currentScreen = "home"
            }
        )

        "random" -> RandomUserScreen(
            onBackClick = {
                currentScreen = "home"
            }
        )

        "universities" -> UniversitiesScreen(
            onBackClick = {
                currentScreen = "home"
            }
        )

        "weather" -> WeatherScreen(
            onBackClick = {
                currentScreen = "home"
            }
        )

        "pokemon" -> PokemonScreen(
            onBackClick = {
                currentScreen = "home"
            }
        )

        "news" -> NewsScreen(
            onBackClick = {
                currentScreen = "home"
            }
        )

        "about" -> AboutScreen(
            onBackClick = {
                currentScreen = "home"
            }
        )

        else -> ToolPlaceholderScreen(
            screen = currentScreen,
            onBackClick = {
                currentScreen = "home"
            }
        )
    }
}

// ============================================
// PANTALLA PRINCIPAL
// ============================================

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        // ====================================
        // ENCABEZADO
        // ====================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "MultiTool",
                color = DarkText,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "RD",
                color = Pink,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Herramientas digitales en un solo lugar",
            modifier = Modifier.fillMaxWidth(),
            color = MutedText,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ====================================
        // TARJETA PRINCIPAL
        // ====================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFE8EAF0),
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // IMAGEN DE CAJA DE HERRAMIENTAS

                Image(
                    painter = painterResource(
                        id = R.drawable.toolbox_banner
                    ),
                    contentDescription = "Caja de herramientas de MultiTool RD",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(165.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Bienvenida a MultiTool RD",
                    color = DarkText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Accede a información, consultas y " +
                            "entretenimiento desde una sola aplicación.",
                    color = MutedText,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ====================================
        // SECCIÓN DE HERRAMIENTAS
        // ====================================

        Text(
            text = "Explora las herramientas",
            modifier = Modifier.fillMaxWidth(),
            color = DarkText,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Selecciona una opción para comenzar",
            modifier = Modifier.fillMaxWidth(),
            color = MutedText,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // ====================================
        // FILA 1
        // ====================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ProfessionalToolCard(
                title = "Género",
                description = "Predicción por nombre",
                icon = Icons.Filled.People,
                accentColor = Pink,
                backgroundColor = PinkBackground,
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigate("gender")
                }
            )

            ProfessionalToolCard(
                title = "Persona aleatoria",
                description = "Perfiles de usuarios",
                icon = Icons.Filled.AccountCircle,
                accentColor = Color(0xFFB45BE5),
                backgroundColor = PurpleBackground,
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigate("random")
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ====================================
        // FILA 2
        // ====================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ProfessionalToolCard(
                title = "Universidades",
                description = "Búsqueda por país",
                icon = Icons.Filled.School,
                accentColor = Color(0xFF16A34A),
                backgroundColor = MintBackground,
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigate("universities")
                }
            )

            ProfessionalToolCard(
                title = "Clima RD",
                description = "Estado del tiempo",
                icon = Icons.Filled.Cloud,
                accentColor = Color(0xFF0284C7),
                backgroundColor = BlueBackground,
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigate("weather")
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ====================================
        // FILA 3
        // ====================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ProfessionalToolCard(
                title = "Pokémon",
                description = "Consulta de Pokémon",
                icon = Icons.Filled.Explore,
                accentColor = Color(0xFFCA8A04),
                backgroundColor = YellowBackground,
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigate("pokemon")
                }
            )

            ProfessionalToolCard(
                title = "Noticias",
                description = "Últimas publicaciones",
                icon = Icons.Filled.Article,
                accentColor = Color(0xFF0891B2),
                backgroundColor = TurquoiseBackground,
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigate("news")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ====================================
        // TARJETA ACERCA DE
        // ====================================

        Card(
            onClick = {
                onNavigate("about")
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Pink.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(20.dp)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = PinkBackground
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White.copy(alpha = 0.75f)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = Pink,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Acerca de",
                        color = DarkText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Desarrolladora e información de contacto",
                        color = MutedText,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = Pink,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ====================================
        // PIE DE PÁGINA
        // ====================================

        Text(
            text = "MULTITOOL RD",
            color = DarkText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Desarrollada con Kotlin y Jetpack Compose",
            color = MutedText,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ============================================
// TARJETA PROFESIONAL CON FONDO PASTEL
// ============================================

@Composable
private fun ProfessionalToolCard(
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = modifier
            .height(145.dp)
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.25f),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ICONO

            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.75f)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // TÍTULO Y DESCRIPCIÓN

            Column {

                Text(
                    text = title,
                    color = DarkText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    color = MutedText,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

// ============================================
// PANTALLA DE RESPALDO
// ============================================

@Composable
fun ToolPlaceholderScreen(
    screen: String,
    onBackClick: () -> Unit
) {

    val title = when (screen) {
        "gender" -> "Predicción de género"
        "random" -> "Persona aleatoria"
        "universities" -> "Universidades"
        "weather" -> "Clima en RD"
        "pokemon" -> "Pokémon"
        "news" -> "Noticias WordPress"
        "about" -> "Acerca de"
        else -> "Herramienta"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                color = DarkText,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Esta herramienta estará disponible " +
                        "en el siguiente paso del desarrollo.",
                color = MutedText,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Pink,
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