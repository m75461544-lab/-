package com.example.ui.components

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
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.quiz.VerseEvaluationResult
import com.example.model.QuestionType
import com.example.model.QuizQuestion
import com.example.ui.theme.GanjinehColors

/**
 * Displays a poetic couplet frame showing the given hemistich and the blanked hemistich slot,
 * plus interactive Persian typing + word-tile assembly tools for VERSE_COMPLETION questions.
 */
@Composable
fun PoeticCoupletFrame(
    question: QuizQuestion,
    userInput: String,
    hasSubmitted: Boolean,
    selectedOptionText: String?,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    if (question.firstHemistich.isBlank() && question.secondHemistich.isBlank()) return

    val firstText = if (!question.blankFirstHemistich) {
        question.firstHemistich
    } else {
        when {
            hasSubmitted -> question.firstHemistich
            question.type == QuestionType.VERSE_COMPLETION && userInput.isNotBlank() -> userInput
            question.type == QuestionType.MULTIPLE_CHOICE && !selectedOptionText.isNullOrBlank() -> selectedOptionText
            else -> "« جای خالی مصرع اول ... »"
        }
    }

    val secondText = if (question.blankFirstHemistich) {
        question.secondHemistich
    } else {
        when {
            hasSubmitted -> question.secondHemistich
            question.type == QuestionType.VERSE_COMPLETION && userInput.isNotBlank() -> userInput
            question.type == QuestionType.MULTIPLE_CHOICE && !selectedOptionText.isNullOrBlank() -> selectedOptionText
            else -> "« جای خالی مصرع دوم ... »"
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("poetic_couplet_frame"),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, ext.goldAccent.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.MenuBook,
                    contentDescription = null,
                    tint = ext.goldAccent,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (question.blankFirstHemistich) {
                        "قاب بیت (تکمیل مصرع اول)"
                    } else {
                        "قاب بیت (تکمیل مصرع دوم)"
                    },
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = ext.goldAccent
                )
            }

            HemistichSlotBox(
                label = "مصرع اول",
                text = firstText,
                isTargetBlank = question.blankFirstHemistich,
                hasSubmitted = hasSubmitted
            )

            HemistichSlotBox(
                label = "مصرع دوم",
                text = secondText,
                isTargetBlank = !question.blankFirstHemistich,
                hasSubmitted = hasSubmitted
            )
        }
    }
}

@Composable
private fun HemistichSlotBox(
    label: String,
    text: String,
    isTargetBlank: Boolean,
    hasSubmitted: Boolean
) {
    val ext = GanjinehColors.current
    val bg = when {
        hasSubmitted && isTargetBlank -> ext.goldAccent.copy(alpha = 0.14f)
        isTargetBlank -> ext.turquoiseInfo.copy(alpha = 0.10f)
        else -> ext.poemSurface
    }
    val border = when {
        hasSubmitted && isTargetBlank -> ext.goldAccent
        isTargetBlank -> ext.turquoiseInfo.copy(alpha = 0.65f)
        else -> ext.cardBorder
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = bg,
        border = BorderStroke(1.dp, border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isTargetBlank) "$label (پاسخ شما)" else "$label (داده شده)",
                style = MaterialTheme.typography.labelSmall,
                color = if (isTargetBlank) ext.turquoiseInfo else ext.goldAccent
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WrittenHemistichInputArea(
    question: QuizQuestion,
    verseUserInput: String,
    hasSubmittedCurrent: Boolean,
    onUpdateVerseInput: (String) -> Unit,
    onAppendHintWord: (String) -> Unit,
    onRemoveLastWord: () -> Unit,
    onRevealNextWord: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    val targetLabel = if (question.blankFirstHemistich) {
        "مصرع اول را بنویسید یا از واژه‌ها بچینید"
    } else {
        "مصرع دوم را بنویسید یا از واژه‌ها بچینید"
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = verseUserInput,
            onValueChange = onUpdateVerseInput,
            enabled = !hasSubmittedCurrent,
            label = { Text(targetLabel) },
            trailingIcon = {
                if (verseUserInput.isNotEmpty() && !hasSubmittedCurrent) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onRemoveLastWord,
                            modifier = Modifier.testTag("verse_undo_word_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Backspace,
                                contentDescription = "حذف آخرین واژه"
                            )
                        }
                        IconButton(
                            onClick = { onUpdateVerseInput("") },
                            modifier = Modifier.testTag("verse_clear_all_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ClearAll,
                                contentDescription = "پاک کردن کامل متن"
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("verse_completion_input"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ext.goldAccent,
                unfocusedBorderColor = ext.cardBorder
            )
        )

        if (!hasSubmittedCurrent && question.wordHints.isNotEmpty()) {
            Text(
                text = "واژه‌چین کمکی (برای چیدمان ضربه بزنید):",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                question.wordHints.forEachIndexed { idx, word ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ext.cardElevated,
                        border = BorderStroke(1.dp, ext.cardBorder),
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onAppendHintWord(word) }
                            .testTag("verse_hint_chip_$idx")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = word,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = onRevealNextWord,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("reveal_hint_word_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Rounded.Lightbulb, contentDescription = null, tint = ext.goldAccent)
                Spacer(Modifier.width(6.dp))
                Text("نمایش واژهٔ آغازین مصرع به عنوان راهنما")
            }
        }
    }
}

@Composable
fun VerseEvalStatsRow(
    eval: VerseEvaluationResult,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "دقت واژگان: ${eval.matchedWords.toPersianDigits()} از ${eval.totalExpectedWords.toPersianDigits()} واژه (${eval.similarityPercent.toPersianDigits()}٪)",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = ext.turquoiseInfo
        )
        if (eval.missingWords.isNotEmpty() && !eval.isCorrect) {
            Text(
                text = "واژه‌های نیازمند دقت بیشتر: ${eval.missingWords.joinToString(" ، ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
