package com.cristian.veya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite
import kotlinx.coroutines.launch

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    ProductosScreen(onBackClick = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(onBackClick: () -> Unit) {
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val api = ApiService.create()
                productos = api.obtenerProductos()
                cargando = false
            } catch (e: Exception) {
                errorMensaje = "Error al conectar: ${e.localizedMessage}"
                cargando = false
            }
        }
    }

    Scaffold(
        topBar = {
            VeyaTopBar(
                title = "Catálogo Veya",
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
            when {
                cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = VeyaPrimary
                    )
                }
                errorMensaje != null -> {
                    Text(
                        text = errorMensaje ?: "Error desconocido",
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(productos) { prod ->
                            ProductoItemCard(prod)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItemCard(p: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = VeyaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = p.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = VeyaDark
                )
                Text(
                    text = "S/ ${p.precio}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = VeyaPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = p.categoria,
                fontSize = 12.sp,
                color = Color(0xFF757575),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Capacidad: ${p.capacidad}",
                    fontSize = 12.sp,
                    color = Color(0xFF424242)
                )
                Text(
                    text = "Batería: ${p.bateria}",
                    fontSize = 12.sp,
                    color = Color(0xFF424242)
                )
            }
        }
    }
}