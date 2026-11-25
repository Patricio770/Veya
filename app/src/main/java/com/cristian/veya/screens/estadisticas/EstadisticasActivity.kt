package com.cristian.veya.screens.estadisticas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristian.veya.VeyaTopBar
import com.cristian.veya.models.EstadisticaDia
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTextSecondary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite

class EstadisticasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[EstadisticasViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    EstadisticasScreen(
                        uiState = uiState,
                        onBackClick = { finish() },
                        onReintentar = { viewModel.fetchEstadisticas() },
                        onLlenarClick = { viewModel.llenarBotella() },
                        onTomarClick = { viewModel.tomarAgua() }
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadisticasScreen(
    uiState: EstadisticasUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit,
    onLlenarClick: () -> Unit,
    onTomarClick: () -> Unit
) {
    Scaffold(
        topBar = {
            VeyaTopBar(
                title = "Estadísticas Veya",
                onBackClick = onBackClick
            )
        },
        containerColor = VeyaBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                EstadisticasUIState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = VeyaPrimary
                    )
                }
                is EstadisticasUIState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.mensaje, color = VeyaDark)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onReintentar,
                            colors = ButtonDefaults.buttonColors(containerColor = VeyaPrimary)
                        ) {
                            Text(text = "Reintentar", color = VeyaWhite)
                        }
                    }
                }
                is EstadisticasUIState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Botella Inteligente Veya",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VeyaDark
                                        )
                                        Text(
                                            text = "Simulación en tiempo real",
                                            fontSize = 12.sp,
                                            color = VeyaTextSecondary
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(VeyaPrimary.copy(alpha = 0.12f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "${uiState.nivelBotellaMl} / ${uiState.capacidadMaxMl} ml",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VeyaPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                val progresoAgua = (uiState.nivelBotellaMl.toFloat() / uiState.capacidadMaxMl.toFloat()).coerceIn(0f, 1f)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(7.dp))
                                        .background(VeyaBackground)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progresoAgua)
                                            .height(14.dp)
                                            .clip(RoundedCornerShape(7.dp))
                                            .background(VeyaPrimary)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = onLlenarClick,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "+ Llenar (250 ml)",
                                            color = VeyaPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Button(
                                        onClick = onTomarClick,
                                        colors = ButtonDefaults.buttonColors(containerColor = VeyaPrimary),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Tomar (-250 ml)",
                                            color = VeyaWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = "Consumo Semanal (ml)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VeyaDark
                                )
                                Text(
                                    text = "Meta diaria: 2500 ml",
                                    fontSize = 12.sp,
                                    color = VeyaTextSecondary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalCanvasBarChart(datos = uiState.dias)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalCanvasBarChart(datos: List<EstadisticaDia>) {
    val textMeasurer = rememberTextMeasurer()
    val maxValor = maxOf(datos.maxOfOrNull { it.mililitros } ?: 3000, 3000).toFloat()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (datos.isEmpty()) return@Canvas

        val leftLabelWidth = 42.dp.toPx()
        val rightValueWidth = 68.dp.toPx()
        val chartWidth = (size.width - leftLabelWidth - rightValueWidth).coerceAtLeast(10f)
        val rowHeight = size.height / datos.size
        val barHeight = (rowHeight * 0.52f).coerceAtMost(26.dp.toPx())

        val goalX = leftLabelWidth + chartWidth * (2500f / maxValor)
        drawLine(
            color = Color(0xFFB0C4C8),
            start = Offset(goalX, 0f),
            end = Offset(goalX, size.height),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
        )

        datos.forEachIndexed { index, item ->
            val centerY = rowHeight * index + rowHeight / 2f
            val topY = centerY - barHeight / 2f

            val labelLayout = textMeasurer.measure(
                text = item.dia,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VeyaDark
                )
            )
            drawText(
                textLayoutResult = labelLayout,
                topLeft = Offset(0f, centerY - labelLayout.size.height / 2f)
            )

            drawRoundRect(
                color = VeyaBackground,
                topLeft = Offset(leftLabelWidth, topY),
                size = Size(chartWidth, barHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )

            val ratio = (item.mililitros / maxValor).coerceIn(0.05f, 1f)
            val filledWidth = chartWidth * ratio
            val barColor = if (index == datos.lastIndex) VeyaPrimary else VeyaPrimary.copy(alpha = 0.65f)

            drawRoundRect(
                color = barColor,
                topLeft = Offset(leftLabelWidth, topY),
                size = Size(filledWidth, barHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )

            val valueLayout = textMeasurer.measure(
                text = "${item.mililitros} ml",
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VeyaDark
                )
            )
            drawText(
                textLayoutResult = valueLayout,
                topLeft = Offset(
                    leftLabelWidth + filledWidth + 8.dp.toPx(),
                    centerY - valueLayout.size.height / 2f
                )
            )
        }
    }
}