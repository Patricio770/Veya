package com.cristian.veya

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.platform.LocalContext

data class DashboardModule(
    val titulo: Int,
    val descripcion: Int,
    val icon: ImageVector
)

private val dashboardModules = listOf(
    DashboardModule(
        R.string.module_bottle_title,
        R.string.module_bottle_desc,
        Icons.Outlined.Bluetooth
    ),
    DashboardModule(
        R.string.module_hydration_title,
        R.string.module_hydration_desc,
        Icons.Outlined.Opacity
    ),
    DashboardModule(
        R.string.module_store_title,
        R.string.module_store_desc,
        Icons.Outlined.Storefront
    ),
    DashboardModule(
        R.string.module_reports_title,
        R.string.module_reports_desc,
        Icons.Outlined.Analytics
    )
)

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeyaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeyaBackground
                ) {
                    DashboardScreen(
                        onModuleClick = {
                            startActivity(Intent(this, TerminosActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onModuleClick: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        containerColor = VeyaBackground,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = AppTheme.spacing.medium)
        ) {
            Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
            Text(
                text = stringResource(R.string.dashboard_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = VeyaDark
            )
            Text(
                text = stringResource(R.string.dashboard_subtitle),
                fontSize = 14.sp,
                color = VeyaTextSecondary
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.large))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = dashboardModules,
                    key = { it.titulo }
                ) { module ->
                    Card(
                        onClick = {
                            val intent = Intent(context, ProductosActivity::class.java)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(AppTheme.spacing.medium),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = module.icon,
                                contentDescription = null,
                                tint = VeyaPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    text = stringResource(id = module.titulo),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VeyaDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(id = module.descripcion),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = VeyaTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppTheme.spacing.small))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.spacing.medium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Veya Smart Bottle",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VeyaDark
                        )
                        Text(
                            text = "Sincronizado hace 5 min",
                            fontSize = 12.sp,
                            color = VeyaTextSecondary
                        )
                    }
                    Text(
                        text = "85% Batería",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VeyaPrimary
                    )
                }
            }
        }
    }
}