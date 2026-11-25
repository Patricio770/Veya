package com.cristian.veya

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.cristian.veya.screens.botella.VincularBotellaActivity
import com.cristian.veya.screens.estadisticas.CentroVeyaActivity
import com.cristian.veya.screens.estadisticas.EstadisticasActivity
import com.cristian.veya.screens.productos.ProductosActivity
import com.cristian.veya.ui.theme.AppTheme
import com.cristian.veya.ui.theme.VeyaBackground
import com.cristian.veya.ui.theme.VeyaDark
import com.cristian.veya.ui.theme.VeyaPrimary
import com.cristian.veya.ui.theme.VeyaTextSecondary
import com.cristian.veya.ui.theme.VeyaTheme
import com.cristian.veya.ui.theme.VeyaWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class DashboardModule(
    val titulo: Int,
    val descripcion: Int,
    val icon: ImageVector,
    val targetActivity: Class<*>? = null
)

private val dashboardModules = listOf(
    DashboardModule(
        titulo = R.string.module_bottle_title,
        descripcion = R.string.module_bottle_desc,
        icon = Icons.Outlined.Bluetooth,
        targetActivity = VincularBotellaActivity::class.java
    ),
    DashboardModule(
        titulo = R.string.module_hydration_title,
        descripcion = R.string.module_hydration_desc,
        icon = Icons.Outlined.Opacity,
        targetActivity = VeyaComponents::class.java
    ),
    DashboardModule(
        titulo = R.string.module_store_title,
        descripcion = R.string.module_store_desc,
        icon = Icons.Outlined.Storefront,
        targetActivity = ProductosActivity::class.java
    ),
    DashboardModule(
        titulo = R.string.module_reports_title,
        descripcion = R.string.module_reports_desc,
        icon = Icons.Outlined.Analytics,
        targetActivity = EstadisticasActivity::class.java
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
                    DashboardScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val coroutineScope = rememberCoroutineScope()

    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var isExpanded by remember { mutableStateOf(false) }
    var isPlayingReverse by remember { mutableStateOf(false) }
    var isVideoCompleted by remember { mutableStateOf(false) }

    var videoAspectRatio by remember { mutableFloatStateOf(9f / 16f) }

    var hasInteracted by remember { mutableStateOf(false) }

    var bannerRect by remember { mutableStateOf<Rect?>(null) }
    var videoViewInstance by remember { mutableStateOf<VideoView?>(null) }

    BackHandler(enabled = isExpanded) {
        if (isVideoCompleted && !isPlayingReverse) {
            isPlayingReverse = true

            videoViewInstance?.apply {
                val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.veya_close}")
                setVideoURI(uri)
                setOnPreparedListener { mp ->
                    mp.isLooping = false
                    start()

                    val halfDuration = (mp.duration / 2L).coerceAtLeast(800L)
                    coroutineScope.launch {
                        delay(halfDuration)
                        isExpanded = false
                    }
                }
                setOnCompletionListener {
                    isPlayingReverse = false
                    isVideoCompleted = false

                    val openUri = Uri.parse("android.resource://${context.packageName}/${R.raw.veya_open}")
                    setVideoURI(openUri)
                    setOnPreparedListener { mpOpen ->
                        mpOpen.isLooping = false
                        seekTo(1)
                    }
                }
            }
        }
    }

    LaunchedEffect(isExpanded) {
        if (isExpanded && !isPlayingReverse) {
            isVideoCompleted = false
            videoViewInstance?.apply {
                val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.veya_open}")
                setVideoURI(uri)
                setOnPreparedListener { mp ->
                    if (mp.videoWidth > 0 && mp.videoHeight > 0) {
                        videoAspectRatio = mp.videoWidth.toFloat() / mp.videoHeight.toFloat()
                    }
                    mp.isLooping = false
                    start()
                }
                setOnCompletionListener {
                    isVideoCompleted = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = VeyaBackground,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .onGloballyPositioned { coordinates ->
                            bannerRect = coordinates.boundsInRoot()
                        }
                )

                Spacer(modifier = Modifier.height(20.dp))

                dashboardModules.chunked(2).forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowModules.forEach { module ->
                            DashboardModuleCard(
                                module = module,
                                onClick = {
                                    if (module.targetActivity != null) {
                                        context.startActivity(Intent(context, module.targetActivity))
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Módulo en desarrollo",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    onClick = {
                        context.startActivity(Intent(context, CentroVeyaActivity::class.java))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VeyaWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.spacing.medium)
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
                                text = "Ecosistema Veya",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = VeyaDark
                            )
                            Text(
                                text = "Puntos de recarga, Bluetooth y garantía",
                                fontSize = 12.sp,
                                color = VeyaTextSecondary
                            )
                        }
                        Text(
                            text = "Explorar →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VeyaPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        bannerRect?.let { rect ->
            val bLeft = with(density) { rect.left.toDp() }
            val bTop = with(density) { rect.top.toDp() }
            val bWidth = with(density) { rect.width.toDp() }
            val bHeight = with(density) { rect.height.toDp() }

            val animDuration = if (hasInteracted) 650 else 0
            val animSpec = tween<androidx.compose.ui.unit.Dp>(animDuration, easing = FastOutSlowInEasing)

            val currentLeft by animateDpAsState(
                targetValue = if (isExpanded) 0.dp else bLeft,
                animationSpec = animSpec,
                label = "left"
            )
            val currentTop by animateDpAsState(
                targetValue = if (isExpanded) 0.dp else bTop,
                animationSpec = animSpec,
                label = "top"
            )
            val currentWidth by animateDpAsState(
                targetValue = if (isExpanded) screenWidth else bWidth,
                animationSpec = animSpec,
                label = "width"
            )
            val currentHeight by animateDpAsState(
                targetValue = if (isExpanded) screenHeight else bHeight,
                animationSpec = animSpec,
                label = "height"
            )
            val currentCorner by animateDpAsState(
                targetValue = if (isExpanded) 0.dp else 20.dp,
                animationSpec = animSpec,
                label = "corner"
            )

            val bannerTargetWidth = maxOf(bWidth, bHeight * videoAspectRatio)
            val bannerTargetHeight = maxOf(bHeight, bWidth / videoAspectRatio)

            val expandedTargetWidth = if (screenWidth / screenHeight < videoAspectRatio) screenWidth else screenHeight * videoAspectRatio
            val expandedTargetHeight = if (screenWidth / screenHeight < videoAspectRatio) screenWidth / videoAspectRatio else screenHeight

            val videoWidthAnim by animateDpAsState(
                targetValue = if (isExpanded) expandedTargetWidth else bannerTargetWidth,
                animationSpec = animSpec,
                label = "videoWidthAnim"
            )
            val videoHeightAnim by animateDpAsState(
                targetValue = if (isExpanded) expandedTargetHeight else bannerTargetHeight,
                animationSpec = animSpec,
                label = "videoHeightAnim"
            )

            Box(
                modifier = Modifier
                    .offset(x = currentLeft, y = currentTop)
                    .size(width = currentWidth, height = currentHeight)
                    .zIndex(if (isExpanded || isPlayingReverse) 100f else 2f)
                    .shadow(elevation = if (isExpanded) 16.dp else 4.dp, shape = RoundedCornerShape(currentCorner))
                    .clip(RoundedCornerShape(currentCorner))
                    .background(Color.Black)
                    .clickable(enabled = !isExpanded && !isPlayingReverse) {
                        hasInteracted = true
                        isExpanded = true
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF2C535A),
                                        Color(0xFF1B353B),
                                        Color(0xFF0F1E22)
                                    )
                                )
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.botella_render),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(40.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            videoViewInstance = this
                            val uri = Uri.parse("android.resource://${ctx.packageName}/${R.raw.veya_open}")
                            setVideoURI(uri)
                            setOnPreparedListener { mp ->
                                if (mp.videoWidth > 0 && mp.videoHeight > 0) {
                                    videoAspectRatio = mp.videoWidth.toFloat() / mp.videoHeight.toFloat()
                                }
                                mp.isLooping = false
                                seekTo(1)
                            }
                        }
                    },
                    modifier = Modifier
                        .requiredSize(width = videoWidthAnim, height = videoHeightAnim)
                )
            }
        }
    }
}

@Composable
private fun DashboardModuleCard(
    module: DashboardModule,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = VeyaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.height(175.dp)
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