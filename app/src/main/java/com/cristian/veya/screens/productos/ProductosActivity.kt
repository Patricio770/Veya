package com.cristian.veya.screens.productos

import android.content.Intent
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristian.veya.VeyaTopBar
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite

class ProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[ProductosViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    ProductosScreen(
                        uiState = uiState,
                        onBackClick = { finish() },
                        onReintentar = { viewModel.fetchProductos() }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductosScreen(
    uiState: ProductosUIState,
    onBackClick: () -> Unit,
    onReintentar: () -> Unit
) {
    val context = LocalContext.current

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
            when (uiState) {
                ProductosUIState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = VeyaPrimary
                    )
                }
                is ProductosUIState.Error -> {
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
                is ProductosUIState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.productos, key = { it.id }) { prod ->
                            ProductoItem(
                                producto = prod,
                                onClick = {
                                    val intent = Intent(context, ProductoDetalleActivity::class.java).apply {
                                        putExtra("nombre", prod.nombre)
                                        putExtra("categoria", prod.categoria)
                                        putExtra("capacidad", prod.capacidad)
                                        putExtra("bateria", prod.bateria)
                                        putExtra("precio", prod.precio)
                                        putExtra("descripcion", prod.descripcion)
                                        putExtra("imagen", prod.imagen)
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}