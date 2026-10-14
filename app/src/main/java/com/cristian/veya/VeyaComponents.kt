package com.cristian.veya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTextSecondary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite

class VeyaComponents : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Scaffold(containerColor = VeyaBackground) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        var inputValue by remember { mutableStateOf("") }
                        var totalMl by remember { mutableIntStateOf(1200) }
                        var showModal by remember { mutableStateOf(false) }

                        VeyaInfoCard(
                            title = "Módulo de Hidratación",
                            subtitle = "Registro diario"
                        ) {
                            Text(
                                text = "Consumo actual: $totalMl ml",
                                color = VeyaPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        VeyaInputField(
                            value = inputValue,
                            onValueChar = { inputValue = it },
                            label = "Cantidad (ml)",
                            placeholder = "",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (inputValue.isNotBlank()) {
                                    showModal = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VeyaPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Registrar toma",
                                fontSize = 16.sp,
                                color = VeyaWhite
                            )
                        }

                        VeyaConfirmModal(
                            show = showModal,
                            title = "Confirmar registro",
                            message = "¿Deseas registrar $inputValue ml a tu consumo diario?",
                            onConfirm = {
                                val cantidad = inputValue.toIntOrNull() ?: 0
                                totalMl += cantidad
                                inputValue = ""
                                showModal = false
                            },
                            onDimiss = {
                                showModal = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VeyaInputField(
    value: String,
    onValueChar: (String) -> Unit,
    label: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChar,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder, color = VeyaTextSecondary) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VeyaPrimary,
            focusedLabelColor = VeyaPrimary,
            unfocusedBorderColor = VeyaTextSecondary.copy(alpha = 0.5f),
            focusedTextColor = VeyaDark,
            unfocusedTextColor = VeyaDark
        ),
        modifier = modifier
    )
}

@Composable
fun VeyaInfoCard(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = VeyaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VeyaDark
            )
            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it, fontSize = 14.sp, color = VeyaTextSecondary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun VeyaConfirmModal(
    show: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDimiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDimiss,
            title = {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = VeyaDark)
            },
            text = {
                Text(text = message, fontSize = 14.sp, color = VeyaDark)
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Confirmar", color = VeyaPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDimiss) {
                    Text(text = "Cancelar", color = VeyaTextSecondary)
                }
            },
            shape = RoundedCornerShape(14.dp),
            containerColor = VeyaWhite
        )
    }
}