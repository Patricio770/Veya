package com.cristian.veya

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cristian.veya.ui.theme.AppTheme
import com.cristian.veya.ui.theme.VeyaTealDark
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaTealDark
                ) {
                    MainScreen(
                        onStartClick = {
                            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onStartClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.spacing.large)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.botella_veya),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = VeyaWhite
            )

            Text(
                text = stringResource(R.string.veya_subtitle),
                fontSize = 14.sp,
                color = VeyaWhite.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VeyaWhite.copy(alpha = 0.15f),
                    contentColor = VeyaWhite
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(52.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_empezar),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = VeyaWhite
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {
                    val intent = Intent(context, TerminosActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "Ver Términos y Condiciones",
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFFB0BEC5)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = AppTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.veya_copyright),
                fontSize = 12.sp,
                color = VeyaWhite.copy(alpha = 0.5f)
            )
        }
    }
}