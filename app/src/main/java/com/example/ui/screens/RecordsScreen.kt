package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.QuestionEntity
import com.example.data.local.QuizRecordEntity
import com.example.ui.components.BadgeChip
import com.example.ui.components.DifficultyBadge
import com.example.ui.components.Figure2DExplanationBox
import com.example.ui.components.LabGlassCard
import com.example.ui.components.LabTopStatusBar
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localized
import com.example.ui.theme.AtpGold
import com.example.ui.theme.BioCyan
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.CoralTrap
import com.example.ui.theme.LabSurface
import com.example.ui.theme.ObsidianBg
import com.example.ui.viewmodel.LabOverviewStats
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    stats: LabOverviewStats,
    records: List<QuizRecordEntity>,
    questions: List<QuestionEntity>,
    pianoEnabled: Boolean,
    onTogglePiano: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleBookmark: (QuestionEntity) -> Unit,
    onStartMistakesReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val strings = LocalAppStrings.current
    val reviewItems = remember(questions) {
        questions.filter { it.isBookmarked || it.lastAttemptCorrect == false || it.timesWrong > 0 }
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                LabTopStatusBar(
                    title = strings.recordsScreenTitle,
                    pianoEnabled = pianoEnabled,
                    onTogglePiano = onTogglePiano,
                    onSelectLanguage = onSelectLanguage,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // KPI Summary
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LabGlassCard(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EmojiEvents,
                            contentDescription = null,
                            tint = AtpGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${stats.bestKonkurPercent.roundToInt()}${strings.percentSymbol}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AtpGold
                        )
                        Text(
                            text = strings.statBestPercent,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    LabGlassCard(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = null,
                            tint = BioCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${stats.highestScore}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = BioCyan
                        )
                        Text(
                            text = strings.highestScoreLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                SecondaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = LabSurface,
                    contentColor = BioCyan
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("${strings.tabHistory} (${records.size})") },
                        modifier = Modifier.testTag("tab_history")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("${strings.tabReview} (${reviewItems.size})") },
                        modifier = Modifier.testTag("tab_review")
                    )
                }
            }

            if (selectedTab == 0) {
                if (records.isEmpty()) {
                    item {
                        LabGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = strings.noRecordsTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = BioCyan
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = strings.noRecordsBody,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(records, key = { it.id }) { rec ->
                        QuizRecordCard(rec)
                    }
                }
            } else {
                if (reviewItems.isNotEmpty()) {
                    item {
                        Button(
                            onClick = onStartMistakesReview,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CoralTrap,
                                contentColor = ObsidianBg
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("records_start_review_button")
                        ) {
                            Text(
                                text = strings.startMistakesReviewButton,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    items(reviewItems, key = { it.id }) { q ->
                        ReviewQuestionCard(
                            question = q,
                            onToggleBookmark = { onToggleBookmark(q) }
                        )
                    }
                } else {
                    item {
                        LabGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = strings.noMistakesTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = ChloroplastEmerald
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = strings.noMistakesBody,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
private fun QuizRecordCard(record: QuizRecordEntity) {
    val strings = LocalAppStrings.current
    val dateStr = remember(record.timestamp) {
        SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault()).format(Date(record.timestamp))
    }
    val lessonLabel = when (record.lessonFilter) {
        1 -> strings.lesson1Short
        2 -> strings.lesson2Short
        else -> strings.lesson1And2Short
    }

    LabGlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = strings.localizeModeName(record.mode),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "$lessonLabel • $dateStr",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BadgeChip(
                text = "${record.konkurPercent.roundToInt()}${strings.percentSymbol} ${strings.konkurPercentBadge}",
                textColor = if (record.konkurPercent >= 50f) ChloroplastEmerald else AtpGold,
                containerColor = if (record.konkurPercent >= 50f) ChloroplastEmerald.copy(alpha = 0.16f) else AtpGold.copy(alpha = 0.16f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${strings.metricCorrect}: ${record.correctCount}",
                style = MaterialTheme.typography.bodySmall,
                color = ChloroplastEmerald
            )
            Text(
                text = "${strings.metricWrong}: ${record.wrongCount}",
                style = MaterialTheme.typography.bodySmall,
                color = CoralTrap
            )
            Text(
                text = "${strings.metricSkipped}: ${record.unansweredCount}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${strings.metricScore}: ${record.scorePoints}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = AtpGold
            )
        }
    }
}

@Composable
private fun ReviewQuestionCard(
    question: QuestionEntity,
    onToggleBookmark: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val strings = LocalAppStrings.current
    val language = LocalAppLanguage.current
    val localizedQuestion = remember(question, language) { question.localized(language) }

    LabGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (localizedQuestion.lastAttemptCorrect == false) CoralTrap.copy(alpha = 0.45f) else BioCyan.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                BadgeChip(text = "${if (localizedQuestion.lesson == 1) strings.lesson1Short else strings.lesson2Short}")
                DifficultyBadge(localizedQuestion.difficulty)
            }
            IconButton(
                onClick = onToggleBookmark,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (localizedQuestion.isBookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = strings.bookmarkDesc,
                    tint = if (localizedQuestion.isBookmarked) AtpGold else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = localizedQuestion.prompt,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        val options = listOf(localizedQuestion.optionA, localizedQuestion.optionB, localizedQuestion.optionC, localizedQuestion.optionD)
        Text(
            text = "${strings.correctAnswerPrefix} ${options.getOrElse(localizedQuestion.correctIndex) { "" }}",
            style = MaterialTheme.typography.labelMedium,
            color = ChloroplastEmerald
        )
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (expanded) strings.closeExplanation else strings.viewExplanation)
            Icon(
                imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = localizedQuestion.explanation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                localizedQuestion.figure2dNote?.let { note ->
                    Figure2DExplanationBox(note = note)
                }
            }
        }
    }
}
