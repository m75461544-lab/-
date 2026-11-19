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
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedMeta
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
    onSelectLanguage: (AppLanguage) -> Unit,
    onStartQuiz: (QuizMode, Int) -> Unit,
    onOpenModel3D: (BioModelType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLesson by rememberSaveable { mutableIntStateOf(0) }
    val strings = LocalAppStrings.current

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
                    title = strings.appTitle,
                    pianoEnabled = pianoEnabled,
                    onTogglePiano = onTogglePiano,
                    onSelectLanguage = onSelectLanguage,
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
                                contentDescription = strings.heroImageDesc,
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
                                text = strings.heroBadge,
                                textColor = AtpGold,
                                containerColor = ObsidianBg.copy(alpha = 0.78f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = strings.heroTitle,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = strings.heroSubtitle,
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
                                        text = strings.heroStartQuiz,
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
                                        text = strings.heroOpen3d,
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
                        label = strings.statOfflineBank,
                        value = "${stats.totalQuestions} ${strings.statQuestionsUnit}",
                        accent = BioCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatMiniCard(
                        label = strings.statBestPercent,
                        value = "${stats.bestKonkurPercent.roundToInt()}${strings.percentSymbol}",
                        accent = AtpGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatMiniCard(
                        label = strings.statMastered,
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
                        text = strings.lessonScopeTitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val chips = listOf(
                            0 to "${strings.lessonBoth} (${stats.totalQuestions})",
                            1 to "${strings.lesson1Full} (${stats.lesson1Count})",
                            2 to "${strings.lesson2Full} (${stats.lesson2Count})"
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
                        text = strings.gameModesTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    GameModeCard(
                        title = strings.modeTitle(QuizMode.TIMED_KONKUR),
                        subtitle = strings.modeSubtitle(QuizMode.TIMED_KONKUR),
                        icon = Icons.Rounded.Timer,
                        accent = BioCyan,
                        badge = strings.badgeCompetitive,
                        testTag = "mode_timed_konkur",
                        onClick = { onStartQuiz(QuizMode.TIMED_KONKUR, selectedLesson) }
                    )
                    GameModeCard(
                        title = strings.modeTitle(QuizMode.FREE_STUDY),
                        subtitle = strings.modeSubtitle(QuizMode.FREE_STUDY),
                        icon = Icons.Rounded.Explore,
                        accent = ChloroplastEmerald,
                        badge = strings.badgeStudy3d,
                        testTag = "mode_free_study",
                        onClick = { onStartQuiz(QuizMode.FREE_STUDY, selectedLesson) }
                    )
                    GameModeCard(
                        title = strings.modeTitle(QuizMode.REVIEW_MISTAKES),
                        subtitle = strings.modeSubtitle(QuizMode.REVIEW_MISTAKES),
                        icon = Icons.Rounded.Bookmark,
                        accent = CoralTrap,
                        badge = "${stats.mistakeCount + stats.bookmarkCount} ${strings.itemsCountUnit}",
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
                            text = strings.models3dCarouselTitle,
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
    val language = LocalAppLanguage.current
    val meta = model.localizedMeta(language)
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
                        contentDescription = meta.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                BadgeChip(
                    text = meta.lessonTag,
                    textColor = BioCyan,
                    containerColor = ObsidianBg.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = meta.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = meta.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}
