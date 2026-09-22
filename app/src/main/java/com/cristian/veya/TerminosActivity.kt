package com.cristian.veya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
                    TerminosScreen(
                        onCerrarClick = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(
    onCerrarClick: () -> Unit
) {
    Scaffold(
        containerColor = VeyaBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.terminos_veya_titulo),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = VeyaDark
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onCerrarClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = VeyaWhite,
                            contentColor = VeyaDark
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VeyaBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = AppTheme.spacing.large)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

            Text(
                text = stringResource(R.string.terminos_veya_subtitulo),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = VeyaPrimary
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

            Text(
                text = stringResource(R.string.terminos_veya_cuerpo),
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = VeyaDark
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

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

            Spacer(modifier = Modifier.height(AppTheme.spacing.large))
        }
    }
}