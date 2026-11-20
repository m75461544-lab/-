package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.HemistichBlankTarget
import com.example.model.Lesson
import com.example.model.LessonPoolScope
import com.example.model.QuizEngineMode
import com.example.model.QuizGenerationConfig
import com.example.ui.theme.GanjinehColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizEngineBuilderCard(
    lessons: List<Lesson>,
    config: QuizGenerationConfig,
    onSelectMode: (QuizEngineMode) -> Unit,
    onUpdateConfig: ((QuizGenerationConfig) -> QuizGenerationConfig) -> Unit,
    onToggleLesson: (String) -> Unit,
    onLaunchQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    val isHemistichMode = config.mode != QuizEngineMode.COMPREHENSIVE_LESSONS
    val questionCountOptions = listOf(5, 8, 12, 15)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("quiz_engine_builder_card"),
        shape = RoundedCornerShape(22.dp),
        color = ext.cardElevated,
        border = BorderStroke(1.2.dp, ext.goldAccent.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = ext.goldAccent
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "موتور هوشمند طراحی آزمون و تکمیل مصرع",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ext.goldAccent
                    )
                    Text(
                        text = config.mode.subtitleFa,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 1. Mode Selection Row
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "۱. انتخاب حالت آزمون:",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                QuizEngineMode.entries.forEach { mode ->
                    val selected = config.mode == mode
                    val icon = when (mode) {
                        QuizEngineMode.COMPREHENSIVE_LESSONS -> Icons.Rounded.AutoStories
                        QuizEngineMode.HEMISTICH_COMPLETION_WRITTEN -> Icons.Rounded.EditNote
                        QuizEngineMode.HEMISTICH_COMPLETION_CHOICE -> Icons.Rounded.Quiz
                    }
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) ext.goldAccent.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            width = if (selected) 1.5.dp else 1.dp,
                            color = if (selected) ext.goldAccent else ext.cardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onSelectMode(mode) }
                            .testTag("engine_mode_${mode.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (selected) ext.goldAccent else ext.turquoiseInfo
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.titleFa,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (selected) ext.goldAccent else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (selected) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = "انتخاب شده",
                                    tint = ext.goldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Scope Selection (All / Memorization / Custom Multi-lesson)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "۲. محدودهٔ درس‌ها:",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LessonPoolScope.entries.forEach { scope ->
                        FilterChip(
                            selected = config.scope == scope,
                            onClick = { onUpdateConfig { it.copy(scope = scope) } },
                            label = { Text(scope.titleFa) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("engine_scope_${scope.name.lowercase()}")
                        )
                    }
                }

                AnimatedVisibility(visible = config.scope == LessonPoolScope.SELECTED_LESSONS) {
                    val candidateLessons = if (isHemistichMode) {
                        lessons.filter { it.isPoetry || it.hasMemorizationPoem }
                    } else {
                        lessons
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = if (config.selectedLessonIds.isEmpty()) {
                                "یک یا چند درس را انتخاب کنید (در صورت عدم انتخاب، از همه طرح می‌شود):"
                            } else {
                                "${config.selectedLessonIds.size.toPersianDigits()} درس انتخاب شده است:"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            candidateLessons.forEach { lesson ->
                                val picked = lesson.id in config.selectedLessonIds
                                FilterChip(
                                    selected = picked,
                                    onClick = { onToggleLesson(lesson.id) },
                                    label = {
                                        Text(
                                            text = "${lesson.numberLabel.toPersianDigits()}: ${lesson.title}",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    modifier = Modifier
                                        .heightIn(min = 48.dp)
                                        .testTag("engine_lesson_chip_${lesson.id}")
                                )
                            }
                        }
                    }
                }
            }

            // 3. Mode-specific options
            AnimatedContent(
                targetState = isHemistichMode,
                label = "engine_mode_options"
            ) { hemistichActive ->
                if (hemistichActive) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "۳. کدام مصرع پنهان شود؟",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            HemistichBlankTarget.entries.forEach { target ->
                                FilterChip(
                                    selected = config.hemistichBlankTarget == target,
                                    onClick = { onUpdateConfig { it.copy(hemistichBlankTarget = target) } },
                                    label = { Text(target.titleFa) },
                                    modifier = Modifier
                                        .heightIn(min = 48.dp)
                                        .testTag("hemistich_target_${target.name.lowercase()}")
                                )
                            }
                        }
                        Text(
                            text = config.hemistichBlankTarget.descriptionFa,
                            style = MaterialTheme.typography.bodySmall,
                            color = ext.turquoiseInfo
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "اولویت با ابیات رسمی حفظی کتاب",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = config.includeMemorizedOnlyInPoetry,
                                onCheckedChange = { enabled ->
                                    onUpdateConfig { it.copy(includeMemorizedOnlyInPoetry = enabled) }
                                },
                                modifier = Modifier.testTag("memorized_only_switch")
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "۳. بخش‌های حاضر در آزمون:",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = config.includeVocabAndSpelling,
                                onClick = {
                                    onUpdateConfig { it.copy(includeVocabAndSpelling = !it.includeVocabAndSpelling) }
                                },
                                label = { Text("واژگان و املا") },
                                modifier = Modifier.heightIn(min = 48.dp)
                            )
                            FilterChip(
                                selected = config.includeDevicesAndMeaning,
                                onClick = {
                                    onUpdateConfig { it.copy(includeDevicesAndMeaning = !it.includeDevicesAndMeaning) }
                                },
                                label = { Text("آرایه و قرابت") },
                                modifier = Modifier.heightIn(min = 48.dp)
                            )
                            FilterChip(
                                selected = config.includeWorkshopAndWisdom,
                                onClick = {
                                    onUpdateConfig { it.copy(includeWorkshopAndWisdom = !it.includeWorkshopAndWisdom) }
                                },
                                label = { Text("کارگاه و گنج حکمت") },
                                modifier = Modifier.heightIn(min = 48.dp)
                            )
                            FilterChip(
                                selected = config.includeHemistichInComprehensive,
                                onClick = {
                                    onUpdateConfig {
                                        it.copy(includeHemistichInComprehensive = !it.includeHemistichInComprehensive)
                                    }
                                },
                                label = { Text("تکمیل مصرع شعر") },
                                modifier = Modifier.heightIn(min = 48.dp)
                            )
                        }
                    }
                }
            }

            // 4. Question Count Selector
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "تعداد پرسش‌ها:",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    questionCountOptions.forEach { count ->
                        val selected = config.questionCount == count
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) ext.goldAccent else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (selected) ext.goldAccent else ext.cardBorder),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onUpdateConfig { it.copy(questionCount = count) } }
                                .testTag("question_count_$count")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${count.toPersianDigits()} سوال",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 5. Launch Button
            Button(
                onClick = onLaunchQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("launch_generated_quiz_button"),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isHemistichMode) {
                        "شروع تمرین تکمیل مصرع (${config.questionCount.toPersianDigits()} بیت)"
                    } else {
                        "ساخت و شروع آزمون (${config.questionCount.toPersianDigits()} پرسش)"
                    },
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
