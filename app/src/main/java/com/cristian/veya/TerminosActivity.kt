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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

class TerminosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    TerminosScreen(onCerrarClick = { finish() })
                }
            }
        }
    }
}

@Composable
fun TerminosScreen(
    onCerrarClick: () -> Unit
) {
    Scaffold(
        containerColor = VeyaBackground,
        topBar = {
            VeyaTopBar(
                title = "Términos y Privacidad",
                onBackClick = onCerrarClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = AppTheme.spacing.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Acuerdo de Servicio del Ecosistema Veya • Versión 2026.4",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = VeyaPrimary
            )

            LegalSectionCard(
                titulo = "1. Uso de la Botella Inteligente y Aplicación",
                cuerpo = "Al vincular tu dispositivo Veya Smart Bottle mediante Bluetooth Low Energy (BLE), autorizas la sincronización automática de tus registros de hidratación, nivel de batería y alertas luminosas en la tapa. Las recomendaciones diarias de consumo son orientativas y no sustituyen el consejo médico profesional."
            )

            LegalSectionCard(
                titulo = "2. Privacidad y Telemetría de Hidratación",
                cuerpo = "Los datos recopilados por el sensor óptico de nivel de agua (mililitros ingeridos, horarios de toma y cumplimiento de metas semanales) se almacenan de forma cifrada en nuestros servidores para generar tus estadísticas personales. Veya no comercializa ni comparte tu historial biométrico con terceros."
            )

            LegalSectionCard(
                titulo = "3. Cuidado Térmico y Resistencia al Agua IPX7",
                cuerpo = "La tapa electrónica cuenta con certificación IPX7 resistente a salpicaduras y lavado manual rápido. No sumerjas la tapa inteligente en agua hirviendo, no la introduzcas en lavavajillas ni en microondas, y seca el puerto magnético antes de conectar la base de carga."
            )

            LegalSectionCard(
                titulo = "4. Garantía Oficial y Soporte Técnico",
                cuerpo = "Todos los modelos Veya Pro, Pure y Sport incluyen 12 meses de garantía oficial contra defectos de fabricación en sensores ópticos, anillo LED y batería interna. Para soporte o reemplazo de filtros de carbón activo, acude a cualquiera de nuestros Puntos Veya autorizados en Lima."
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onCerrarClick,
                colors = ButtonDefaults.buttonColors(containerColor = VeyaPrimary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_entendido),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VeyaWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun LegalSectionCard(
    titulo: String,
    cuerpo: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = VeyaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VeyaDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = cuerpo,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = VeyaTextSecondary
            )
        }
    }
}