package com.cristian.veya.screens.botella

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BluetoothConnected
import androidx.compose.material.icons.outlined.BluetoothSearching
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cristian.veya.VeyaTopBar
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTextSecondary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite
import kotlinx.coroutines.delay

class VincularBotellaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    VincularBotellaScreen(onBackClick = { finish() })
                }
            }
        }
    }
}

@Composable
private fun VincularBotellaScreen(onBackClick: () -> Unit) {
    var isScanning by remember { mutableStateOf(true) }
    var scanAttempt by remember { mutableIntStateOf(0) }

    LaunchedEffect(scanAttempt) {
        isScanning = true
        delay(3500L)
        isScanning = false
    }

    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale1"
    )
    val pulseAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha1"
    )
    val pulseScale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, delayMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale2"
    )
    val pulseAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, delayMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha2"
    )

    Scaffold(
        topBar = {
            VeyaTopBar(
                title = "Vincular Mi Botella",
                onBackClick = onBackClick
            )
        },
        containerColor = VeyaBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isScanning) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .scale(pulseScale1)
                                .clip(CircleShape)
                                .background(VeyaPrimary.copy(alpha = pulseAlpha1))
                        )
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .scale(pulseScale2)
                                .clip(CircleShape)
                                .background(VeyaPrimary.copy(alpha = pulseAlpha2))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .clip(CircleShape)
                            .background(if (isScanning) VeyaPrimary else VeyaDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isScanning) {
                                Icons.Outlined.BluetoothSearching
                            } else {
                                Icons.Outlined.BluetoothConnected
                            },
                            contentDescription = null,
                            tint = VeyaWhite,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isScanning) {
                        "Rastreando botella Veya..."
                    } else {
                        "Veya Pro Matte Black conectada"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VeyaDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isScanning) {
                        "Acerca tu botella inteligente a menos de 30 cm de tu teléfono para sincronizar los sensores de hidratación."
                    } else {
                        "Sincronización Bluetooth Low Energy activa. Los sensores ópticos y de temperatura están listos."
                    },
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = VeyaTextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            if (!isScanning) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estado del dispositivo",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = VeyaDark
                            )
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = VeyaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        DeviceSpecRow(label = "Modelo", value = "Veya Pro 750 ml")
                        DeviceSpecRow(label = "Batería restante", value = "85% (25 días)")
                        DeviceSpecRow(label = "Señal Bluetooth", value = "Excelente (-46 dBm)")
                        DeviceSpecRow(label = "Firmware sensor", value = "v2.4.1 Actualizado")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!isScanning) {
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(containerColor = VeyaPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Confirmar y volver al panel",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VeyaWhite
                        )
                    }
                }

                OutlinedButton(
                    onClick = { scanAttempt++ },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = if (isScanning) "Reiniciar búsqueda" else "Volver a rastrear dispositivo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VeyaPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceSpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = VeyaTextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = VeyaDark)
    }
}