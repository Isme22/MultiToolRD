package com.example.multitoolrd

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================
// COLORES MULTITOOL RD
// ============================================

private val AboutPink = Color(0xFFFF56A4)
private val AboutYellow = Color(0xFFFCE93A)
private val AboutMint = Color(0xFF95F5BA)
private val AboutPurple = Color(0xFFD48CFF)
private val AboutBackground = Color(0xFFF8FAFC)
private val AboutDark = Color(0xFF172554)
private val AboutMuted = Color(0xFF64748B)

// ============================================
// PANTALLA ACERCA DE
// ============================================

@Composable
fun AboutScreen(onBackClick: () -> Unit) {

    val context = LocalContext.current

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AboutBackground)
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // TÍTULO

            Text(
                text = "ACERCA DE",
                color = AboutDark,
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Conoce a la desarrolladora de MultiTool RD",
                color = AboutMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            // ====================================
            // TARJETA DE PERFIL
            // ====================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AboutPink.copy(alpha = 0.15f)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // FOTO DE PERFIL

                    Image(
                        painter = painterResource(
                            id = R.drawable.profile_photo
                        ),
                        contentDescription = "Fotografía de Ismeiri Adames Varona",
                        modifier = Modifier
                            .size(135.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // NOMBRE

                    Text(
                        text = "Ismeiri Adames Varona",
                        color = AboutDark,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // PROFESIÓN

                    Text(
                        text = "Desarrolladora de Software y Aplicaciones Móviles",
                        color = AboutDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = AboutPurple.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(50.dp)
                    ) {

                        Text(
                            text = "Estudiante del ITLA",
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 9.dp
                            ),
                            color = AboutDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ====================================
            // PRESENTACIÓN PERSONAL
            // ====================================

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

                    Text(
                        text = "SOBRE MÍ",
                        color = AboutDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Soy estudiante de Programación en el Instituto " +
                                "Tecnológico de Las Américas (ITLA), con interés " +
                                "en el desarrollo de software y aplicaciones móviles. " +
                                "Me apasiona aprender nuevas tecnologías y crear " +
                                "soluciones digitales que sean útiles, accesibles " +
                                "y fáciles de utilizar.",
                        color = AboutMuted,
                        fontSize = 14.sp,
                        lineHeight = 23.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ====================================
            // ÁREAS DE INTERÉS
            // ====================================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AboutMint.copy(alpha = 0.40f)
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "ÁREAS DE INTERÉS",
                        color = AboutDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "• Desarrollo de aplicaciones móviles\n" +
                                "• Desarrollo de software\n" +
                                "• Diseño de interfaces de usuario\n" +
                                "• Aprendizaje de nuevas tecnologías",
                        color = AboutDark,
                        fontSize = 14.sp,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ====================================
            // INFORMACIÓN DE CONTACTO
            // ====================================

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

                    Text(
                        text = "CONTACTO PROFESIONAL",
                        color = AboutDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Correo electrónico",
                        color = AboutMuted,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "ismeirivarona@gmail.com",
                        color = AboutDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Teléfono",
                        color = AboutMuted,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "829-334-5163",
                        color = AboutDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // BOTÓN ENVIAR CORREO

                    Button(
                        onClick = {

                            val intent = Intent(
                                Intent.ACTION_SENDTO
                            ).apply {

                                data = Uri.parse(
                                    "mailto:ismeirivarona@gmail.com"
                                )
                            }

                            if (intent.resolveActivity(
                                    context.packageManager
                                ) != null
                            ) {
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AboutPink,
                            contentColor = Color.White
                        )
                    ) {

                        Text(
                            text = "ENVIAR CORREO",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // BOTÓN LLAMAR

                    Button(
                        onClick = {

                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:8293345163")
                            )

                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AboutYellow,
                            contentColor = AboutDark
                        )
                    ) {

                        Text(
                            text = "LLAMAR",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ====================================
            // INFORMACIÓN DE LA APLICACIÓN
            // ====================================

            Text(
                text = "MultiTool RD",
                color = AboutDark,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Aplicación académica desarrollada con Kotlin y Jetpack Compose",
                color = AboutMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ====================================
        // BOTÓN VOLVER AL MENÚ
        // ====================================

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AboutDark,
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