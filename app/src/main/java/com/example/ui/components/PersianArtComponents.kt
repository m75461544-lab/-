package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GanjinehColors
import com.example.ui.viewmodel.TimerSessionState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun Int.toPersianDigits(): String = toString().toPersianDigits()

fun String.toPersianDigits(): String {
    val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    val sb = StringBuilder(length)
    for (ch in this) {
        if (ch in '0'..'9') {
            sb.append(persianDigits[ch - '0'])
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}

fun formatDurationPersian(totalSeconds: Int): String {
    val safe = totalSeconds.coerceAtLeast(0)
    val mins = safe / 60
    val secs = safe % 60
    val raw = "%02d:%02d".format(mins, secs)
    return raw.toPersianDigits()
}

@Composable
fun ShamsaMedallionCanvas(
    modifier: Modifier = Modifier,
    accentColor: Color = GanjinehColors.current.goldAccent,
    secondaryColor: Color = GanjinehColors.current.turquoiseInfo
) {
    val infinite = rememberInfiniteTransition(label = "shamsa_rotation")
    val angle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shamsa_angle"
    )
    val pulse by infinite.animateFloat(
        initialValue = 0.72f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shamsa_pulse"
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension * 0.45f

        drawCircle(
            color = accentColor.copy(alpha = 0.08f * pulse),
            radius = radius * 1.05f,
            center = center
        )

        rotate(degrees = angle, pivot = center) {
            val points = 12
            val path = Path()
            for (i in 0 until points * 2) {
                val r = if (i % 2 == 0) radius else radius * 0.62f
                val rad = (i * PI / points).toFloat()
                val x = center.x + r * cos(rad)
                val y = center.y + r * sin(rad)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(
                path = path,
                color = accentColor.copy(alpha = 0.42f),
                style = Stroke(width = 2.2f)
            )
        }

        rotate(degrees = -angle * 0.65f, pivot = center) {
            val innerPoints = 8
            val innerPath = Path()
            val innerR = radius * 0.64f
            for (i in 0 until innerPoints * 2) {
                val r = if (i % 2 == 0) innerR else innerR * 0.56f
                val rad = (i * PI / innerPoints).toFloat()
                val x = center.x + r * cos(rad)
                val y = center.y + r * sin(rad)
                if (i == 0) innerPath.moveTo(x, y) else innerPath.lineTo(x, y)
            }
            innerPath.close()
            drawPath(
                path = innerPath,
                color = secondaryColor.copy(alpha = 0.45f),
                style = Stroke(width = 1.8f)
            )
        }

        drawCircle(
            color = accentColor.copy(alpha = 0.7f * pulse),
            radius = radius * 0.14f,
            center = center
        )
    }
}

@Composable
fun PersianHeroBanner(
    title: String,
    subtitle: String,
    badgeText: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val ext = GanjinehColors.current
    val infinite = rememberInfiniteTransition(label = "hero_shimmer")
    val shimmerShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_shift"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.35f)),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            ext.heroGradientStart,
                            ext.heroGradientEnd,
                            ext.heroGradientStart.copy(alpha = 0.9f)
                        )
                    )
                )
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val archPath = Path().apply {
                    val w = size.width
                    val h = size.height
                    moveTo(w * 0.06f, h)
                    lineTo(w * 0.06f, h * 0.42f)
                    cubicTo(
                        w * 0.06f, h * 0.14f,
                        w * 0.24f, h * 0.08f,
                        w * 0.50f, h * 0.05f
                    )
                    cubicTo(
                        w * 0.76f, h * 0.08f,
                        w * 0.94f, h * 0.14f,
                        w * 0.94f, h * 0.42f
                    )
                    lineTo(w * 0.94f, h)
                }
                drawPath(
                    path = archPath,
                    color = ext.goldAccent.copy(alpha = 0.14f + 0.08f * shimmerShift),
                    style = Stroke(width = 2.5f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ext.goldAccent.copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * (0.15f + 0.7f * shimmerShift), size.height * 0.25f),
                        radius = size.maxDimension * 0.45f
                    ),
                    radius = size.maxDimension * 0.45f,
                    center = Offset(size.width * (0.15f + 0.7f * shimmerShift), size.height * 0.25f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = ext.goldAccent.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = ext.goldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = ext.goldAccent,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (trailingContent != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    trailingContent()
                }
            }
        }
    }
}

@Composable
fun StudyTimerBannerCard(
    timerState: TimerSessionState,
    onTogglePlayPause: () -> Unit,
    onFinishAndReport: () -> Unit,
    onQuickSimulate30Min: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    val animatedProgress by animateFloatAsState(
        targetValue = timerState.progressFraction,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "timer_ring_progress"
    )
    val pulseTransition = rememberInfiniteTransition(label = "live_dot")
    val dotAlpha by pulseTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = ext.cardElevated,
        border = BorderStroke(
            1.2.dp,
            if (timerState.isRunning) ext.goldAccent.copy(alpha = 0.65f) else ext.cardBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier.size(54.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = 5.dp.toPx()
                            drawArc(
                                color = ext.cardBorder.copy(alpha = 0.5f),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round),
                                topLeft = Offset(stroke / 2f, stroke / 2f),
                                size = Size(size.width - stroke, size.height - stroke)
                            )
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(ext.goldAccent, ext.turquoiseInfo, ext.goldAccent)
                                ),
                                startAngle = -90f,
                                sweepAngle = animatedProgress * 360f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round),
                                topLeft = Offset(stroke / 2f, stroke / 2f),
                                size = Size(size.width - stroke, size.height - stroke)
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.Timer,
                            contentDescription = "زمان‌سنج مطالعه",
                            tint = ext.goldAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (timerState.isRunning) {
                                            ext.emeraldSuccess.copy(alpha = dotAlpha)
                                        } else {
                                            ext.goldAccent.copy(alpha = 0.5f)
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (timerState.isRunning) {
                                    "در حال ثبت مطالعه (هدف ۳۰ دقیقه‌ای)"
                                } else {
                                    "زمان‌سنج مطالعه آماده است"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = ext.goldAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${formatDurationPersian(timerState.elapsedSeconds)} از ${formatDurationPersian(timerState.targetSeconds)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "در پایان ۳۰ دقیقه یا پس از توقف، گزارش پیشرفت و آزمون تکمیل مصرع ساخته می‌شود.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("timer_play_pause_button")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = ext.goldAccent.copy(alpha = 0.2f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (timerState.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = if (timerState.isRunning) "توقف موقت زمان‌سنج" else "ادامه زمان‌سنج",
                                tint = ext.goldAccent
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onFinishAndReport,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("finish_study_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "پایان مطالعه و آزمون",
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                OutlinedButton(
                    onClick = onQuickSimulate30Min,
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("simulate_30m_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, ext.turquoiseInfo.copy(alpha = 0.65f))
                ) {
                    Text(
                        text = "مرور ۳۰ دقیقه‌ای",
                        style = MaterialTheme.typography.labelMedium,
                        color = ext.turquoiseInfo,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun StaggeredAppear(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 360,
                delayMillis = (index.coerceAtMost(8)) * 45
            )
        ) + slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            initialOffsetY = { it / 5 }
        )
    ) {
        content()
    }
}

@Composable
fun CelebrationConfettiCanvas(
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    val particles = remember {
        val rng = Random(42)
        List(36) {
            Triple(
                rng.nextFloat(), // x fraction
                rng.nextFloat(), // speed multiplier
                rng.nextInt(3)   // color index
            )
        }
    }
    val infinite = rememberInfiniteTransition(label = "confetti")
    val fall by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_fall"
    )

    Canvas(modifier = modifier) {
        val palette = listOf(ext.goldAccent, ext.turquoiseInfo, ext.emeraldSuccess)
        particles.forEachIndexed { idx, (xFrac, speed, colorIdx) ->
            val yFrac = (fall * (0.65f + speed * 0.7f) + (idx * 0.08f)) % 1f
            val x = size.width * xFrac + sin((yFrac * 8f + idx).toDouble()).toFloat() * 14f
            val y = size.height * yFrac
            drawCircle(
                color = palette[colorIdx].copy(alpha = 0.72f * (1f - yFrac * 0.6f)),
                radius = 4.dp.toPx() + (idx % 3) * 1.5f,
                center = Offset(x, y)
            )
        }
    }
}
