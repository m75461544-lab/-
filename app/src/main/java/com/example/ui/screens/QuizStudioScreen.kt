package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppThemeMode
import com.example.model.Lesson
import com.example.model.QuizEngineMode
import com.example.model.QuizGenerationConfig
import com.example.model.StudyProgressState
import com.example.ui.components.PersianHeroBanner
import com.example.ui.components.QuizEngineBuilderCard
import com.example.ui.components.StaggeredAppear
import com.example.ui.components.formatDurationPersian
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.GanjinehColors

@Composable
fun QuizStudioScreen(
    lessons: List<Lesson>,
    progressState: StudyProgressState,
    quizConfig: QuizGenerationConfig,
    onSelectEngineMode: (QuizEngineMode) -> Unit,
    onUpdateQuizConfig: ((QuizGenerationConfig) -> QuizGenerationConfig) -> Unit,
    onToggleSelectedLesson: (String) -> Unit,
    onLaunchConfiguredQuiz: () -> Unit,
    onStartCustomLessonQuiz: (Lesson) -> Unit,
    onStartAdvancedQuiz: () -> Unit,
    onOpenThemePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    val totalStudySecs = progressState.lessonStudySeconds.values.sum()
    val accuracyPercent = if (progressState.totalQuestionsAnswered > 0) {
        (progressState.totalCorrectAnswers * 100) / progressState.totalQuestionsAnswered
    } else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("quiz_studio_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.widthIn(max = 820.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PersianHeroBanner(
                        title = "آزمون‌ساز هوشمند و کارنامه",
                        subtitle = "طراحی خودکار سوال از دروس فارسی، حالت اختصاصی تکمیل مصرع شعر و کارنامهٔ مطالعه",
                        badgeText = "تم فعال: ${progressState.themeMode.titleFa}"
                    )

                    // Analytics grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatTile(
                            label = "مجموع مطالعه",
                            value = formatDurationPersian(totalStudySecs),
                            icon = Icons.Rounded.Timer,
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            label = "میانگین دقت",
                            value = "${accuracyPercent.toPersianDigits()}٪",
                            icon = Icons.Rounded.EmojiEvents,
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            label = "بهترین نمره",
                            value = "${progressState.bestScorePercent.toPersianDigits()}٪",
                            icon = Icons.Rounded.AutoAwesome,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Configurable Quiz Generation Engine & Dedicated Hemistich Completion Mode
                    QuizEngineBuilderCard(
                        lessons = lessons,
                        config = quizConfig,
                        onSelectMode = onSelectEngineMode,
                        onUpdateConfig = onUpdateQuizConfig,
                        onToggleLesson = onToggleSelectedLesson,
                        onLaunchQuiz = onLaunchConfiguredQuiz
                    )

                    OutlinedButton(
                        onClick = onStartAdvancedQuiz,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("studio_advanced_quiz_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, ext.turquoiseInfo.copy(alpha = 0.6f))
                    ) {
                        Icon(Icons.Rounded.Quiz, contentDescription = null, tint = ext.turquoiseInfo)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "چالش ۱۰ سوالهٔ سطح پیشرفته و المپیادی",
                            color = ext.turquoiseInfo
                        )
                    }

                    // Theme Switcher Banner
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = ext.cardElevated,
                        border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable(onClick = onOpenThemePicker)
                            .testTag("studio_theme_picker_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Palette,
                                    contentDescription = "انتخاب پوسته ظاهری",
                                    tint = ext.goldAccent
                                )
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "انتخاب پوستهٔ ظاهری (${AppThemeMode.entries.size.toPersianDigits()} تم متنوع)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "فعال: ${progressState.themeMode.titleFa}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "تغییر تم",
                                style = MaterialTheme.typography.labelLarge,
                                color = ext.goldAccent
                            )
                        }
                    }

                    Text(
                        text = "آزمون فوری از یک درس مشخص:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ext.goldAccent,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        itemsIndexed(lessons, key = { _, l -> l.id }) { idx, lesson ->
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.widthIn(max = 820.dp)) {
                    StaggeredAppear(index = idx) {
                        val secs = progressState.lessonStudySeconds[lesson.id] ?: 0
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, ext.cardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onStartCustomLessonQuiz(lesson) }
                                .testTag("studio_quiz_lesson_${lesson.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${lesson.numberLabel.toPersianDigits()}: ${lesson.title}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = buildString {
                                            append(if (lesson.hasMemorizationPoem) "شامل تکمیل مصرع حفظی" else "شامل واژگان، آرایه و تحلیل متن")
                                            if (secs > 0) {
                                                append(" • مطالعه: ")
                                                append(formatDurationPersian(secs))
                                            }
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = ext.goldAccent.copy(alpha = 0.16f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.PlayArrow,
                                            contentDescription = "شروع آزمون درس",
                                            tint = ext.goldAccent,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = "طرح سوال",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = ext.goldAccent
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatTile(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, ext.cardBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = ext.goldAccent,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
