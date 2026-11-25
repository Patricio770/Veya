package com.cristian.veya.screens.productos

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.cristian.veya.R
import com.cristian.veya.VeyaTopBar
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTextSecondary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite

class ProductoDetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val nombre = intent.getStringExtra("nombre") ?: ""
        val categoria = intent.getStringExtra("categoria") ?: ""
        val capacidad = intent.getStringExtra("capacidad") ?: ""
        val bateria = intent.getStringExtra("bateria") ?: ""
        val precio = intent.getStringExtra("precio") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val imagen = intent.getStringExtra("imagen") ?: ""

        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    ProductoDetalleScreen(
                        nombre = nombre,
                        categoria = categoria,
                        capacidad = capacidad,
                        bateria = bateria,
                        precio = precio,
                        descripcion = descripcion,
                        imagen = imagen,
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductoDetalleScreen(
    nombre: String,
    categoria: String,
    capacidad: String,
    bateria: String,
    precio: String,
    descripcion: String,
    imagen: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            VeyaTopBar(
                title = "Detalle de Producto",
                onBackClick = onBackClick
            )
        },
        containerColor = VeyaBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(VeyaWhite)
            ) {
                val placeholder = painterResource(R.drawable.botella_render)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagen)
                        .crossfade(true)
                        .build(),
                    contentDescription = nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = placeholder,
                    error = placeholder
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = categoria,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VeyaPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = nombre,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VeyaDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "S/ $precio",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VeyaPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SpecChip(title = "Capacidad", value = capacidad, modifier = Modifier.weight(1f))
                        SpecChip(title = "Batería", value = bateria, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Descripción",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = VeyaDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = descripcion,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = VeyaTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecChip(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(VeyaBackground)
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 11.sp, color = VeyaTextSecondary)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VeyaDark)
    }
}