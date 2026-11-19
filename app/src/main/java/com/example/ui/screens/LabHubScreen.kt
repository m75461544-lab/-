package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bio3d.Bio3DPreviewRenderer
import com.example.bio3d.BioModelType
import com.example.ui.components.BadgeChip
import com.example.ui.components.LabGlassCard
import com.example.ui.components.LabTopStatusBar
import com.example.ui.theme.AtpGold
import com.example.ui.theme.BioCyan
import com.example.ui.theme.BioCyanContainer
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.CoralTrap
import com.example.ui.theme.LabBorder
import com.example.ui.theme.LabSurface
import com.example.ui.theme.LabSurfaceElevated
import com.example.ui.theme.ObsidianBg
import com.example.ui.viewmodel.LabOverviewStats
import com.example.ui.viewmodel.QuizMode
import kotlin.math.roundToInt

@Composable
fun LabHubScreen(
    stats: LabOverviewStats,
    pianoEnabled: Boolean,
    onTogglePiano: () -> Unit,
    onStartQuiz: (QuizMode, Int) -> Unit,
    onOpenModel3D: (BioModelType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLesson by rememberSaveable { mutableIntStateOf(0) }

    val heroBitmap by produceState<ImageBitmap?>(initialValue = null) {
        value = Bio3DPreviewRenderer.getPreviewBitmap(BioModelType.EUKARYOTIC_CELL, 640, 320)
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LabTopStatusBar(
                    title = "زیست‌نگار ۳بعدی",
                    pianoEnabled = pianoEnabled,
                    onTogglePiano = onTogglePiano,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Hero 3D Biology Lab Banner
            item {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = LabSurface,
                    border = BorderStroke(1.dp, BioCyan.copy(alpha = 0.45f)),
                    tonalElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        heroBitmap?.let { bmp ->
                            Image(
                                bitmap = bmp,
                                contentDescription = "نمای سه‌بعدی آزمایشگاه زیست دهم",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            ObsidianBg.copy(alpha = 0.48f),
                                            ObsidianBg.copy(alpha = 0.90f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            BadgeChip(
                                text = "فصل ۱ زیست دهم • گفتار ۱ و ۲ (سطح کنکور)",
                                textColor = AtpGold,
                                containerColor = ObsidianBg.copy(alpha = 0.78f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "آزمایشگاه سه‌بعدی و نبرد تست‌های سخت کنکور",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "بررسی ساختارهای درونی یاخته، غشا و اندامک‌ها از همه زوایا با قابلیت شفاف‌سازی X-Ray و نوای آرامش‌بخش پیانو.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onStartQuiz(QuizMode.TIMED_KONKUR, selectedLesson) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BioCyan,
                                        contentColor = ObsidianBg
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("hero_start_quiz_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "شروع چالش کنکور",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                                OutlinedButton(
                                    onClick = { onOpenModel3D(BioModelType.EUKARYOTIC_CELL) },
                                    border = BorderStroke(1.dp, ChloroplastEmerald.copy(alpha = 0.7f)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = ChloroplastEmerald
                                    ),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("hero_open_3d_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Science,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "آزمایشگاه ۳بعدی",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Stats Strip
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatMiniCard(
                        label = "بانک تست آفلاین",
                        value = "${stats.totalQuestions} سوال",
                        accent = BioCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatMiniCard(
                        label = "بهترین درصد کنکور",
                        value = "${stats.bestKonkurPercent.roundToInt()}٪",
                        accent = AtpGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatMiniCard(
                        label = "تسلط کامل",
                        value = "${stats.masteredCount}/${stats.totalQuestions}",
                        accent = ChloroplastEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Lesson Scope Selector (Strictly Lesson 1 & Lesson 2 of Chapter 1)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "محدوده گفتارهای فصل ۱ (دنیای زنده):",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val chips = listOf(
                            0 to "هر دو گفتار (${stats.totalQuestions})",
                            1 to "گفتار ۱: زیست‌شناسی چیست؟ (${stats.lesson1Count})",
                            2 to "گفتار ۲: گستره حیات (${stats.lesson2Count})"
                        )
                        items(chips) { (lessonId, label) ->
                            FilterChip(
                                selected = selectedLesson == lessonId,
                                onClick = { selectedLesson = lessonId },
                                label = {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BioCyanContainer,
                                    selectedLabelColor = BioCyan
                                ),
                                modifier = Modifier.testTag("lesson_filter_$lessonId")
                            )
                        }
                    }
                }
            }

            // Game Modes Section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "انتخاب حالت بازی و تمرین",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    GameModeCard(
                        title = QuizMode.TIMED_KONKUR.titleFa,
                        subtitle = QuizMode.TIMED_KONKUR.subtitleFa,
                        icon = Icons.Rounded.Timer,
                        accent = BioCyan,
                        badge = "رقابتی • رکورد",
                        testTag = "mode_timed_konkur",
                        onClick = { onStartQuiz(QuizMode.TIMED_KONKUR, selectedLesson) }
                    )
                    GameModeCard(
                        title = QuizMode.FREE_STUDY.titleFa,
                        subtitle = QuizMode.FREE_STUDY.subtitleFa,
                        icon = Icons.Rounded.Explore,
                        accent = ChloroplastEmerald,
                        badge = "آموزشی • ۳بعدی",
                        testTag = "mode_free_study",
                        onClick = { onStartQuiz(QuizMode.FREE_STUDY, selectedLesson) }
                    )
                    GameModeCard(
                        title = QuizMode.REVIEW_MISTAKES.titleFa,
                        subtitle = QuizMode.REVIEW_MISTAKES.subtitleFa,
                        icon = Icons.Rounded.Bookmark,
                        accent = CoralTrap,
                        badge = "${stats.mistakeCount + stats.bookmarkCount} مورد",
                        testTag = "mode_review_mistakes",
                        onClick = { onStartQuiz(QuizMode.REVIEW_MISTAKES, selectedLesson) }
                    )
                }
            }

            // 3D Biological Models Quick Carousel
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "مدل‌های سه‌بعدی تشریحی (بررسی ۳۶۰°)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = Icons.Rounded.Psychology,
                            contentDescription = null,
                            tint = BioCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(BioModelType.entries, key = { it.id }) { model ->
                            Model3DQuickCard(
                                model = model,
                                onClick = { onOpenModel3D(model) }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}

@Composable
private fun StatMiniCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    LabGlassCard(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = accent
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun GameModeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    badge: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        color = LabSurface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.38f)),
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = accent.copy(alpha = 0.16f),
                border = BorderStroke(1.dp, accent.copy(alpha = 0.4f)),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BadgeChip(
                        text = badge,
                        textColor = accent,
                        containerColor = accent.copy(alpha = 0.14f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun Model3DQuickCard(
    model: BioModelType,
    onClick: () -> Unit
) {
    val thumb by produceState<ImageBitmap?>(initialValue = null, model) {
        value = Bio3DPreviewRenderer.getPreviewBitmap(model, 280, 180)
    }

    Surface(
        shape = MaterialTheme.shapes.large,
        color = LabSurfaceElevated,
        border = BorderStroke(1.dp, LabBorder),
        modifier = Modifier
            .width(200.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .testTag("quick_model_${model.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(106.dp)
                    .background(ObsidianBg)
            ) {
                thumb?.let {
                    Image(
                        bitmap = it,
                        contentDescription = model.titleFa,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                BadgeChip(
                    text = model.lessonTag,
                    textColor = BioCyan,
                    containerColor = ObsidianBg.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = model.titleFa,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = model.subtitleFa,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}
