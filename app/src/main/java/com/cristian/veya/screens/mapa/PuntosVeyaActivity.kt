package com.cristian.veya.screens.mapa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristian.veya.VeyaTopBar
import com.cristian.veya.models.PuntoVeya
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTextSecondary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class PuntosVeyaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[PuntosViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    PuntosVeyaScreen(
                        uiState = uiState,
                        onBackClick = { finish() },
                        onReintentar = { viewModel.fetchPuntos() }
                    )
                }
            }
        }
    }
}

@Composable
private fun PuntosVeyaScreen(
    uiState: PuntosUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = {
            VeyaTopBar(
                title = "Puntos Veya Lima",
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
                PuntosUIState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = VeyaPrimary
                    )
                }
                is PuntosUIState.Error -> {
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
                is PuntosUIState.Success -> {
                    MapaPuntosContent(puntos = uiState.puntos)
                }
            }
        }
    }
}

@Composable
private fun MapaPuntosContent(puntos: List<PuntoVeya>) {
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }
    var markersById: Map<Int, Marker> by remember { mutableStateOf(mapOf()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                Configuration.getInstance().userAgentValue = ctx.packageName
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(12.5)
                    controller.setCenter(GeoPoint(-12.1050, -77.0150))

                    val nuevosMarcadores = mutableMapOf<Int, Marker>()
                    puntos.forEach { punto ->
                        val marker = Marker(this).apply {
                            position = GeoPoint(punto.latitud, punto.longitud)
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = punto.nombre
                            snippet = "${punto.distrito} - ${punto.direccion}"
                        }
                        overlays.add(marker)
                        nuevosMarcadores[punto.id] = marker
                    }
                    markersById = nuevosMarcadores
                    mapViewInstance = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        LazyRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(puntos, key = { it.id }) { punto ->
                Card(
                    onClick = {
                        mapViewInstance?.controller?.animateTo(
                            GeoPoint(punto.latitud, punto.longitud),
                            15.5,
                            700L
                        )
                        markersById[punto.id]?.showInfoWindow()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.width(240.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = punto.distrito,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = VeyaPrimary
                        )
                        Text(
                            text = punto.nombre,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = VeyaDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = punto.direccion,
                            fontSize = 12.sp,
                            color = VeyaTextSecondary
                        )
                    }
                }
            }
        }
    }
}