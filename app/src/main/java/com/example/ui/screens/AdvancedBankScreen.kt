package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.QuestionDifficulty
import com.example.model.QuestionDomain
import com.example.model.QuestionType
import com.example.model.QuizQuestion
import com.example.ui.components.PersianHeroBanner
import com.example.ui.components.StaggeredAppear
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.GanjinehColors

@Composable
fun AdvancedBankScreen(
    questions: List<QuizQuestion>,
    selectedDomain: QuestionDomain?,
    selectedDifficulty: QuestionDifficulty?,
    onSelectDomain: (QuestionDomain?) -> Unit,
    onSelectDifficulty: (QuestionDifficulty?) -> Unit,
    onStartBankQuiz: (QuestionDomain?, QuestionDifficulty?) -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    val filtered = remember(questions, selectedDomain, selectedDifficulty) {
        questions.filter { q ->
            (selectedDomain == null || q.domain == selectedDomain) &&
                (selectedDifficulty == null || q.difficulty == selectedDifficulty)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("advanced_bank_list"),
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
                        title = "بانک سوال پیشرفته و کنکوری",
                        subtitle = "سوالات سطح بالا و ترکیبی از تمامی قلمروها با پاسخنامهٔ تشریحی کامل",
                        badgeText = "بخش سوم • ${questions.size.toPersianDigits()} پرسش تراز بالا"
                    )

                    // Domain filters
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedDomain == null,
                            onClick = { onSelectDomain(null) },
                            label = { Text("همهٔ قلمروها") }
                        )
                        QuestionDomain.entries.forEach { domain ->
                            FilterChip(
                                selected = selectedDomain == domain,
                                onClick = { onSelectDomain(if (selectedDomain == domain) null else domain) },
                                label = { Text(domain.titleFa) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    // Difficulty filters
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedDifficulty == null,
                            onClick = { onSelectDifficulty(null) },
                            label = { Text("همهٔ سطوح") }
                        )
                        QuestionDifficulty.entries.forEach { diff ->
                            FilterChip(
                                selected = selectedDifficulty == diff,
                                onClick = { onSelectDifficulty(if (selectedDifficulty == diff) null else diff) },
                                label = { Text(diff.titleFa) }
                            )
                        }
                    }

                    Button(
                        onClick = { onStartBankQuiz(selectedDomain, selectedDifficulty) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("start_advanced_bank_quiz_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Rounded.PlayCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "برگزاری آزمون تعاملی (${filtered.size.toPersianDigits()} سوال موجود)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        itemsIndexed(filtered, key = { _, q -> q.id }) { idx, question ->
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.widthIn(max = 820.dp)) {
                    StaggeredAppear(index = idx) {
                        AdvancedQuestionPreviewCard(
                            number = idx + 1,
                            question = question
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdvancedQuestionPreviewCard(
    number: Int,
    question: QuizQuestion
) {
    val ext = GanjinehColors.current
    var showAnswer by rememberSaveable(question.id) { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (question.difficulty == QuestionDifficulty.OLYMPIAD) {
                ext.goldAccent.copy(alpha = 0.5f)
            } else {
                ext.cardBorder
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { showAnswer = !showAnswer }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ext.goldAccent.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = "سوال ${number.toPersianDigits()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = ext.goldAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ext.turquoiseInfo.copy(alpha = 0.14f)
                    ) {
                        Text(
                            text = question.difficulty.titleFa,
                            style = MaterialTheme.typography.labelSmall,
                            color = ext.turquoiseInfo,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Icon(
                    imageVector = if (showAnswer) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = if (showAnswer) "بستن پاسخ تشریحی" else "مشاهده پاسخ تشریحی",
                    tint = ext.goldAccent
                )
            }

            Text(
                text = "${question.lessonTitle} • ${question.domain.titleFa}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = question.prompt,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (question.type == QuestionType.MULTIPLE_CHOICE) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    question.options.forEachIndexed { idx, opt ->
                        val isCorrect = idx == question.correctOptionIndex
                        val optionBorder = if (showAnswer && isCorrect) {
                            BorderStroke(1.5.dp, ext.emeraldSuccess)
                        } else {
                            BorderStroke(1.dp, ext.cardBorder.copy(alpha = 0.6f))
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (showAnswer && isCorrect) {
                                ext.emeraldSuccess.copy(alpha = 0.14f)
                            } else {
                                ext.cardElevated.copy(alpha = 0.55f)
                            },
                            border = optionBorder,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${(idx + 1).toPersianDigits()}) $opt",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showAnswer) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = ext.poemSurface,
                    border = BorderStroke(1.dp, ext.emeraldSuccess.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.WorkspacePremium,
                                contentDescription = null,
                                tint = ext.emeraldSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = if (question.type == QuestionType.VERSE_COMPLETION) {
                                    "پاسخ صحیح: ${question.expectedVerseText}"
                                } else {
                                    "پاسخنامهٔ تشریحی (گزینهٔ ${(question.correctOptionIndex + 1).toPersianDigits()}):"
                                },
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = ext.emeraldSuccess
                            )
                        }
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
