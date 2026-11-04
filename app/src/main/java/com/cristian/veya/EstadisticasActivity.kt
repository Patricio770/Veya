package com.cristian.veya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cristian.veya.ui.theme.AppTheme
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTextSecondary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite

data class HydrationDay(
    val dia: String,
    val intakeMl: Int,
    val targetMl: Int = 2500
)

class EstadisticasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    EstadisticasScreen(onBackClick = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen(onBackClick: () -> Unit) {
    var selectedFilterIndex by remember { mutableIntStateOf(1) } // 0: Día, 1: Semana, 2: Mes
    val filters = listOf("Día", "Semana", "Mes")

    val weeklyRecords = remember {
        listOf(
            HydrationDay("Lun", 2100),
            HydrationDay("Mar", 2550),
            HydrationDay("Mié", 2700),
            HydrationDay("Jue", 1900),
            HydrationDay("Vie", 2600),
            HydrationDay("Sáb", 2450),
            HydrationDay("Dom", 2800)
        )
    }

    var selectedDay by remember { mutableStateOf(weeklyRecords.last()) }

    Scaffold(
        topBar = {
            VeyaTopBar(
                title = "Estadísticas Veya",
                onBackClick = onBackClick
            )
        },
        containerColor = VeyaBackground,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppTheme.spacing.medium)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Selector temporal de píldora (Día / Semana / Mes)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3ECEE))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                filters.forEachIndexed { index, title ->
                    val isSelected = selectedFilterIndex == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (isSelected) VeyaWhite else Color.Transparent)
                            .clickable { selectedFilterIndex = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) VeyaDark else VeyaTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Tarjetas de Resumen Rápido (3 métricas en fila)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricKpiCard(
                    title = "Promedio",
                    value = "2.46 L",
                    trend = "+12% vs ayer",
                    icon = Icons.Outlined.WaterDrop,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiCard(
                    title = "Meta Lograda",
                    value = "86%",
                    trend = "6/7 días",
                    icon = Icons.Outlined.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiCard(
                    title = "Racha",
                    value = "5 Días",
                    trend = "Récord: 9",
                    icon = Icons.Outlined.LocalFireDepartment,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Gráfico Semanal con Meta Punteada de 2.5L
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Consumo Semanal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = VeyaDark
                            )
                            Text(
                                text = "Meta diaria: 2.5 L",
                                fontSize = 12.sp,
                                color = VeyaTextSecondary
                            )
                        }

                        // Badge dinámico con el valor del día seleccionado
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(VeyaPrimary.copy(alpha = 0.12f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${selectedDay.dia}:${selectedDay.intakeMl} ml",
                                color = VeyaPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    InteractiveBarChart(
                        data = weeklyRecords,
                        selected = selectedDay,
                        onSelect = { selectedDay = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Telemetría de los Sensores de la Botella
            Text(
                text = "Telemetría de la Botella",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VeyaDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TelemetryCard(
                    title = "Aperturas de Tapa",
                    value = "14 tomas",
                    detail = "Frecuencia diaria",
                    icon = Icons.Outlined.Sensors,
                    modifier = Modifier.weight(1f)
                )
                TelemetryCard(
                    title = "Volumen Promedio",
                    value = "175 ml",
                    detail = "Por cada sorbo",
                    icon = Icons.Outlined.Timeline,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TelemetryCard(
                    title = "Intervalo Promedio",
                    value = "48 min",
                    detail = "Tiempo entre tomas",
                    icon = Icons.Outlined.AccessTime,
                    modifier = Modifier.weight(1f)
                )
                TelemetryCard(
                    title = "Pico de Consumo",
                    value = "16:30 hrs",
                    detail = "Horario con más tomas",
                    icon = Icons.Outlined.WaterDrop,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Tarjeta de Diagnóstico Inteligente
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VeyaPrimary.copy(alpha = 0.08f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(VeyaPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = VeyaWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Hábito Detectado",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = VeyaDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estás concentrando el 65% de tu consumo de agua después de las 4:00 PM. Tu botella vibrará suavemente por la mañana para ayudarte a distribuir mejor tu hidratación.",
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = VeyaDark.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InteractiveBarChart(
    data: List<HydrationDay>,
    selected: HydrationDay,
    onSelect: (HydrationDay) -> Unit
) {
    val maxScaleMl = 3000f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val targetY = size.height * (1f - (2500f / maxScaleMl))
            drawLine(
                color = Color(0xFFB0C4C8),
                start = Offset(0f, targetY),
                end = Offset(size.width, targetY),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEach { item ->
                val isSelected = item == selected
                val barProgress = (item.intakeMl / maxScaleMl).coerceIn(0.08f, 1f)

                val animatedProgress = remember { Animatable(0f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = barProgress,
                        animationSpec = tween(700, easing = FastOutSlowInEasing)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelect(item) }
                ) {
                    Box(
                        modifier = Modifier
                            .width(26.dp)
                            .height(130.dp * animatedProgress.value)
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(
                                if (isSelected) VeyaPrimary else VeyaPrimary.copy(alpha = 0.35f)
                            )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.dia,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) VeyaPrimary else VeyaTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricKpiCard(
    title: String,
    value: String,
    trend: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = VeyaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(VeyaPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VeyaPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VeyaDark
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = VeyaDark
            )
            Text(
                text = trend,
                fontSize = 10.sp,
                color = VeyaPrimary
            )
        }
    }
}

@Composable
private fun TelemetryCard(
    title: String,
    value: String,
    detail: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = VeyaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(VeyaPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VeyaPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = VeyaDark
                )
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = VeyaDark
                )
                Text(
                    text = detail,
                    fontSize = 10.sp,
                    color = VeyaTextSecondary
                )
            }
        }
    }
}