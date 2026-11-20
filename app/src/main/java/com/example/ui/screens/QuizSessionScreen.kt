package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.model.QuestionType
import com.example.model.QuizSessionState
import com.example.ui.components.CelebrationConfettiCanvas
import com.example.ui.components.PoeticCoupletFrame
import com.example.ui.components.VerseEvalStatsRow
import com.example.ui.components.WrittenHemistichInputArea
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.GanjinehColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSessionOverlayScreen(
    quizState: QuizSessionState,
    onSelectOption: (Int) -> Unit,
    onUpdateVerseInput: (String) -> Unit,
    onAppendHintWord: (String) -> Unit,
    onRemoveLastWord: () -> Unit,
    onRevealNextWord: () -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onCloseQuiz: () -> Unit
) {
    BackHandler(onBack = onCloseQuiz)
    val ext = GanjinehColors.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = quizState.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = quizState.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onCloseQuiz,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("close_quiz_session_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "بستن آزمون"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (quizState.isFinished) {
                val total = quizState.questions.size.coerceAtLeast(1)
                val percent = (quizState.correctCount * 100) / total

                Box(modifier = Modifier.fillMaxSize()) {
                    if (percent >= 50) {
                        CelebrationConfettiCanvas(modifier = Modifier.fillMaxSize())
                    }
                    Column(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(28.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.5.dp, ext.goldAccent.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.EmojiEvents,
                                    contentDescription = null,
                                    tint = ext.goldAccent,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "آزمون به پایان رسید!",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = ext.goldAccent
                                )
                                Text(
                                    text = "نمرهٔ شما: ${quizState.correctCount.toPersianDigits()} از ${total.toPersianDigits()} (${percent.toPersianDigits()}٪)",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = when {
                                        percent >= 85 -> "درخشیدید! تسلط شما بر مطالب و شعرهای فارسی دهم فوق‌العاده است."
                                        percent >= 60 -> "بسیار خوب! با مرور نکات تشریحی می‌توانید به ۱۰۰٪ تسلط برسید."
                                        else -> "شروع خوبی بود؛ پیشنهاد می‌کنیم درس‌نامه و شعرهای حفظی را یک بار دیگر مرور کنید."
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(6.dp))
                                Button(
                                    onClick = onCloseQuiz,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("finish_quiz_return_button"),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(
                                        text = "بازگشت و ادامهٔ مطالعه",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                val question = quizState.currentQuestion
                if (question == null) {
                    Text(
                        text = "سوالی یافت نشد.",
                        modifier = Modifier.padding(24.dp)
                    )
                    return@Scaffold
                }

                val progressFraction by animateFloatAsState(
                    targetValue = (quizState.currentIndex + 1).toFloat() / quizState.questions.size.coerceAtLeast(1),
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "quiz_progress"
                )

                Column(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "پرسش ${(quizState.currentIndex + 1).toPersianDigits()} از ${quizState.questions.size.toPersianDigits()}",
                            style = MaterialTheme.typography.labelLarge,
                            color = ext.goldAccent
                        )
                        Text(
                            text = "${question.lessonTitle} • ${question.difficulty.titleFa}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = ext.goldAccent,
                        trackColor = ext.cardBorder.copy(alpha = 0.4f)
                    )

                    AnimatedContent(
                        targetState = question.id,
                        label = "quiz_question_transition"
                    ) { _ ->
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = ext.poemSurface,
                                border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.45f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = ext.turquoiseInfo.copy(alpha = 0.16f)
                                    ) {
                                        Text(
                                            text = question.domain.titleFa,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = ext.turquoiseInfo,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = question.prompt,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // Dedicated poetic couplet frame when question carries hemistichs
                            if (question.firstHemistich.isNotBlank() || question.secondHemistich.isNotBlank()) {
                                val selectedOptText = quizState.selectedOptionIndex?.let { idx ->
                                    question.options.getOrNull(idx)
                                }
                                PoeticCoupletFrame(
                                    question = question,
                                    userInput = quizState.verseUserInput,
                                    hasSubmitted = quizState.hasSubmittedCurrent,
                                    selectedOptionText = selectedOptText
                                )
                            }

                            if (question.type == QuestionType.MULTIPLE_CHOICE) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    question.options.forEachIndexed { idx, opt ->
                                        val isSelected = quizState.selectedOptionIndex == idx
                                        val isCorrectOption = idx == question.correctOptionIndex
                                        val bgColor = when {
                                            quizState.hasSubmittedCurrent && isCorrectOption -> ext.emeraldSuccess.copy(alpha = 0.18f)
                                            quizState.hasSubmittedCurrent && isSelected && !isCorrectOption -> ext.crimsonError.copy(alpha = 0.18f)
                                            isSelected -> ext.goldAccent.copy(alpha = 0.18f)
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                        val borderColor = when {
                                            quizState.hasSubmittedCurrent && isCorrectOption -> ext.emeraldSuccess
                                            quizState.hasSubmittedCurrent && isSelected && !isCorrectOption -> ext.crimsonError
                                            isSelected -> ext.goldAccent
                                            else -> ext.cardBorder
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = bgColor,
                                            border = BorderStroke(if (isSelected) 1.8.dp else 1.dp, borderColor),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .clickable(enabled = !quizState.hasSubmittedCurrent) {
                                                    onSelectOption(idx)
                                                }
                                                .testTag("quiz_option_$idx")
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(14.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "${(idx + 1).toPersianDigits()}) $opt",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                if (quizState.hasSubmittedCurrent && isCorrectOption) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.CheckCircle,
                                                        contentDescription = "گزینهٔ صحیح",
                                                        tint = ext.emeraldSuccess
                                                    )
                                                } else if (quizState.hasSubmittedCurrent && isSelected && !isCorrectOption) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Cancel,
                                                        contentDescription = "گزینهٔ نادرست",
                                                        tint = ext.crimsonError
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                WrittenHemistichInputArea(
                                    question = question,
                                    verseUserInput = quizState.verseUserInput,
                                    hasSubmittedCurrent = quizState.hasSubmittedCurrent,
                                    onUpdateVerseInput = onUpdateVerseInput,
                                    onAppendHintWord = onAppendHintWord,
                                    onRemoveLastWord = onRemoveLastWord,
                                    onRevealNextWord = onRevealNextWord
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = quizState.hasSubmittedCurrent) {
                        val accent = if (quizState.currentIsCorrect) ext.emeraldSuccess else ext.crimsonError
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = accent.copy(alpha = 0.14f),
                            border = BorderStroke(1.2.dp, accent),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (quizState.currentIsCorrect) Icons.Rounded.AutoAwesome else Icons.Rounded.Cancel,
                                        contentDescription = null,
                                        tint = accent
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = quizState.currentFeedback,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = accent
                                    )
                                }
                                quizState.lastVerseEval?.let { eval ->
                                    VerseEvalStatsRow(eval = eval)
                                }
                                if (question.type == QuestionType.VERSE_COMPLETION) {
                                    Text(
                                        text = "مصرع دقیق: ${question.expectedVerseText}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = ext.goldAccent
                                    )
                                }
                                Text(
                                    text = question.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    if (!quizState.hasSubmittedCurrent) {
                        val canSubmit = if (question.type == QuestionType.MULTIPLE_CHOICE) {
                            quizState.selectedOptionIndex != null
                        } else {
                            quizState.verseUserInput.isNotBlank()
                        }
                        Button(
                            onClick = onSubmitAnswer,
                            enabled = canSubmit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_quiz_answer_button"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "بررسی و ثبت پاسخ",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    } else {
                        Button(
                            onClick = onNextQuestion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("next_quiz_question_button"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = if (quizState.currentIndex + 1 < quizState.questions.size) {
                                    "پرسش بعدی"
                                } else {
                                    "مشاهدهٔ کارنامهٔ نهایی"
                                },
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
