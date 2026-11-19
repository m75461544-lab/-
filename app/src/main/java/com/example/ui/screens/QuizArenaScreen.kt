package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bio3d.BioModelType
import com.example.data.local.QuestionEntity
import com.example.ui.components.BadgeChip
import com.example.ui.components.Bio3DInspectorCard
import com.example.ui.components.DifficultyBadge
import com.example.ui.components.Figure2DExplanationBox
import com.example.ui.components.LabGlassCard
import com.example.ui.components.LabTopStatusBar
import com.example.ui.theme.AtpGold
import com.example.ui.theme.BioCyan
import com.example.ui.theme.BioCyanContainer
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.CoralTrap
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.LabBorder
import com.example.ui.theme.LabSurface
import com.example.ui.theme.LabSurfaceElevated
import com.example.ui.theme.ObsidianBg
import com.example.ui.viewmodel.BioLab3DState
import com.example.ui.viewmodel.QuizMode
import com.example.ui.viewmodel.QuizSessionState
import kotlin.math.roundToInt

@Composable
fun QuizArenaScreen(
    quizState: QuizSessionState,
    labState: BioLab3DState,
    pianoEnabled: Boolean,
    onTogglePiano: () -> Unit,
    onStartQuiz: (QuizMode, Int) -> Unit,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onExitQuiz: () -> Unit,
    onToggleBookmark: (QuestionEntity) -> Unit,
    onXrayChange: (Float) -> Unit,
    onSelectPart: (String) -> Unit,
    onToggleAutoRotate: () -> Unit,
    onZoomChange: (Float) -> Unit,
    onResetCamera: () -> Unit,
    onOpenRecords: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (quizState.isActive) {
        BackHandler { onExitQuiz() }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        when {
            quizState.isFinished -> {
                QuizSummaryView(
                    state = quizState,
                    onPlayAgain = { onStartQuiz(quizState.mode, quizState.lessonFilter) },
                    onOpenRecords = {
                        onExitQuiz()
                        onOpenRecords()
                    },
                    onBackToSetup = onExitQuiz
                )
            }

            quizState.isActive && quizState.currentQuestion != null -> {
                ActiveQuestionView(
                    state = quizState,
                    question = quizState.currentQuestion!!,
                    labState = labState,
                    onSelectOption = onSelectOption,
                    onSubmitAnswer = onSubmitAnswer,
                    onNextQuestion = onNextQuestion,
                    onExitQuiz = onExitQuiz,
                    onToggleBookmark = { onToggleBookmark(quizState.currentQuestion!!) },
                    onXrayChange = onXrayChange,
                    onSelectPart = onSelectPart,
                    onToggleAutoRotate = onToggleAutoRotate,
                    onZoomChange = onZoomChange,
                    onResetCamera = onResetCamera
                )
            }

            else -> {
                QuizLauncherSetupView(
                    emptyMessage = quizState.emptyReviewMessage,
                    pianoEnabled = pianoEnabled,
                    onTogglePiano = onTogglePiano,
                    onStartQuiz = onStartQuiz
                )
            }
        }
    }
}

@Composable
private fun QuizLauncherSetupView(
    emptyMessage: String?,
    pianoEnabled: Boolean,
    onTogglePiano: () -> Unit,
    onStartQuiz: (QuizMode, Int) -> Unit
) {
    var selectedMode by rememberSaveable { mutableStateOf(QuizMode.TIMED_KONKUR) }
    var selectedLesson by rememberSaveable { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 680.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LabTopStatusBar(
                title = "صحنه چالش کنکور",
                pianoEnabled = pianoEnabled,
                onTogglePiano = onTogglePiano,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (!emptyMessage.isNullOrBlank()) {
            item {
                LabGlassCard(
                    borderColor = AtpGold.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AtpGold
                    )
                }
            }
        }

        item {
            LabGlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "تنظیمات شروع آزمون فصل ۱ زیست دهم",
                    style = MaterialTheme.typography.titleMedium,
                    color = BioCyan
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "انتخاب گفتار:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0 to "هر دو گفتار", 1 to "گفتار ۱", 2 to "گفتار ۲").forEach { (id, label) ->
                        FilterChip(
                            selected = selectedLesson == id,
                            onClick = { selectedLesson = id },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BioCyanContainer,
                                selectedLabelColor = BioCyan
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "انتخاب سبک بازی:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuizMode.entries.forEach { mode ->
                        val active = selectedMode == mode
                        Surface(
                            onClick = { selectedMode = mode },
                            shape = MaterialTheme.shapes.medium,
                            color = if (active) BioCyanContainer else LabSurfaceElevated,
                            border = BorderStroke(1.dp, if (active) BioCyan else LabBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("setup_mode_${mode.name}")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = mode.titleFa,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (active) BioCyan else MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = mode.subtitleFa,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onStartQuiz(selectedMode, selectedLesson) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BioCyan,
                        contentColor = ObsidianBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("launch_quiz_button")
                ) {
                    Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ورود به میدان رقابت",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveQuestionView(
    state: QuizSessionState,
    question: QuestionEntity,
    labState: BioLab3DState,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onExitQuiz: () -> Unit,
    onToggleBookmark: () -> Unit,
    onXrayChange: (Float) -> Unit,
    onSelectPart: (String) -> Unit,
    onToggleAutoRotate: () -> Unit,
    onZoomChange: (Float) -> Unit,
    onResetCamera: () -> Unit
) {
    var show3DViewer by rememberSaveable(question.id) { mutableStateOf(true) }
    val options = listOf(question.optionA, question.optionB, question.optionC, question.optionD)
    val optionLabels = listOf("۱", "۲", "۳", "۴")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 680.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Quiz HUD Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onExitQuiz,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("quiz_exit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "خروج از آزمون",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (state.mode == QuizMode.TIMED_KONKUR) {
                            val urgent = state.remainingSeconds <= 15
                            BadgeChip(
                                text = "⏱ ${state.remainingSeconds} ثانیه",
                                textColor = if (urgent) CoralTrap else BioCyan,
                                containerColor = if (urgent) CoralContainer else BioCyanContainer
                            )
                        }
                        if (state.currentStreak > 0) {
                            BadgeChip(
                                text = "🔥 ${state.currentStreak} پی‌درپی",
                                textColor = AtpGold,
                                containerColor = AtpGold.copy(alpha = 0.16f)
                            )
                        }
                        BadgeChip(
                            text = "${state.currentIndex + 1}/${state.questions.size}",
                            textColor = ChloroplastEmerald,
                            containerColor = EmeraldContainer
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("quiz_bookmark_button")
                    ) {
                        Icon(
                            imageVector = if (question.isBookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                            contentDescription = "نشان‌گذاری سوال",
                            tint = if (question.isBookmarked) AtpGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { (state.currentIndex + 1).toFloat() / state.questions.size.coerceAtLeast(1) },
                    color = BioCyan,
                    trackColor = LabSurfaceElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
            }
        }

        // Toggleable 3D Model Inspector Attached to Question
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    onClick = { show3DViewer = !show3DViewer },
                    shape = MaterialTheme.shapes.medium,
                    color = LabSurfaceElevated,
                    border = BorderStroke(1.dp, BioCyan.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("toggle_question_3d_box")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Visibility,
                                contentDescription = null,
                                tint = BioCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "شکل سه‌بعدی مرتبط: ${BioModelType.fromId(question.modelType).titleFa}",
                                style = MaterialTheme.typography.labelLarge,
                                color = BioCyan
                            )
                        }
                        Icon(
                            imageVector = if (show3DViewer) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = null,
                            tint = BioCyan
                        )
                    }
                }

                AnimatedVisibility(visible = show3DViewer) {
                    Bio3DInspectorCard(
                        modelType = BioModelType.fromId(question.modelType),
                        xrayLevel = labState.xrayLevel,
                        highlightedPartId = labState.highlightedPartId,
                        autoRotate = labState.autoRotate,
                        zoomDistance = labState.zoomDistance,
                        resetCounter = labState.resetCounter,
                        onXrayChange = onXrayChange,
                        onSelectPart = onSelectPart,
                        onToggleAutoRotate = onToggleAutoRotate,
                        onZoomChange = onZoomChange,
                        onResetCamera = onResetCamera,
                        remoteImageUrl = question.remoteImageUrl,
                        viewportHeight = 225.dp,
                        show2DNoteIfPresent = question.figure2dNote
                    )
                }
            }
        }

        // Question Prompt Card
        item {
            LabGlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BadgeChip(text = "فصل ۱ • گفتار ${question.lesson}")
                    DifficultyBadge(question.difficulty)
                    BadgeChip(
                        text = question.questionType,
                        textColor = ChloroplastEmerald,
                        containerColor = EmeraldContainer
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = question.prompt,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // 4 Konkur Answer Options
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                options.forEachIndexed { idx, optionText ->
                    val isSelected = state.selectedOptionIndex == idx
                    val isCorrect = idx == question.correctIndex
                    val submitted = state.isAnswerSubmitted

                    val borderColor: Color
                    val bgColor: Color
                    val labelColor: Color

                    when {
                        submitted && isCorrect -> {
                            borderColor = ChloroplastEmerald
                            bgColor = EmeraldContainer.copy(alpha = 0.72f)
                            labelColor = ChloroplastEmerald
                        }
                        submitted && isSelected && !isCorrect -> {
                            borderColor = CoralTrap
                            bgColor = CoralContainer.copy(alpha = 0.72f)
                            labelColor = CoralTrap
                        }
                        isSelected -> {
                            borderColor = BioCyan
                            bgColor = BioCyanContainer.copy(alpha = 0.72f)
                            labelColor = BioCyan
                        }
                        else -> {
                            borderColor = LabBorder
                            bgColor = LabSurface
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    }

                    Surface(
                        onClick = { onSelectOption(idx) },
                        enabled = !submitted,
                        shape = MaterialTheme.shapes.medium,
                        color = bgColor,
                        border = BorderStroke(1.5.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("option_card_$idx")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = labelColor.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, labelColor.copy(alpha = 0.6f)),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = optionLabels[idx],
                                        style = MaterialTheme.typography.labelLarge,
                                        color = labelColor
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Submit or Next + Detailed Konkur Explanation
        item {
            if (!state.isAnswerSubmitted) {
                Button(
                    onClick = onSubmitAnswer,
                    enabled = state.selectedOptionIndex != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BioCyan,
                        contentColor = ObsidianBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_answer_button")
                ) {
                    Text(
                        text = "ثبت پاسخ و مشاهده تحلیل کنکوری",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            AnimatedVisibility(
                visible = state.isAnswerSubmitted,
                enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                    slideInVertically(spring(stiffness = Spring.StiffnessMediumLow)) { it / 4 }
            ) {
                val answeredCorrectly = state.selectedOptionIndex == question.correctIndex
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LabGlassCard(
                        borderColor = if (answeredCorrectly) ChloroplastEmerald else CoralTrap,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = if (answeredCorrectly) ChloroplastEmerald else CoralTrap,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when {
                                    state.selectedOptionIndex == null -> "زمان تمام شد! پاسخ صحیح: گزینه ${optionLabels[question.correctIndex]}"
                                    answeredCorrectly -> "آفرین! پاسخ کاملاً درست است"
                                    else -> "دام کنکوری! پاسخ صحیح: گزینه ${optionLabels[question.correctIndex]}"
                                },
                                style = MaterialTheme.typography.titleSmall,
                                color = if (answeredCorrectly) ChloroplastEmerald else CoralTrap
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        question.figure2dNote?.let { note ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Figure2DExplanationBox(note = note)
                        }
                    }

                    Button(
                        onClick = onNextQuestion,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChloroplastEmerald,
                            contentColor = ObsidianBg
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("next_question_button")
                    ) {
                        Text(
                            text = if (state.currentIndex + 1 >= state.questions.size) {
                                "پایان آزمون و مشاهده کارنامه"
                            } else {
                                "سوال بعدی"
                            },
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }
    }
}

@Composable
private fun QuizSummaryView(
    state: QuizSessionState,
    onPlayAgain: () -> Unit,
    onOpenRecords: () -> Unit,
    onBackToSetup: () -> Unit
) {
    val total = state.questions.size.coerceAtLeast(1)
    val netPercent = (((state.correctCount * 3f - state.wrongCount) / (total * 3f)) * 100f)
        .coerceIn(-33.3f, 100f)
        .roundToInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 680.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LabGlassCard(
                borderColor = BioCyan.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "کارنامه نهایی ${state.mode.titleFa}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = BioCyan
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "درصد کنکوری (با احتساب نمره منفی): $netPercent٪",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (netPercent >= 50) ChloroplastEmerald else AtpGold
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryMetric("درست", "${state.correctCount}", ChloroplastEmerald)
                    SummaryMetric("غلط", "${state.wrongCount}", CoralTrap)
                    SummaryMetric("نزده", "${state.unansweredCount}", MaterialTheme.colorScheme.onSurfaceVariant)
                    SummaryMetric("امتیاز", "${state.scorePoints}", AtpGold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = null,
                        tint = AtpGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "بیشترین زنجیره پاسخ درست: ${state.bestStreak}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AtpGold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        tint = BioCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "زمان کل: ${state.totalElapsedSeconds} ثانیه",
                        style = MaterialTheme.typography.bodySmall,
                        color = BioCyan
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onPlayAgain,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BioCyan,
                        contentColor = ObsidianBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("summary_play_again")
                ) {
                    Icon(imageVector = Icons.Rounded.Replay, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("شروع مجدد این آزمون")
                }

                OutlinedButton(
                    onClick = onOpenRecords,
                    border = BorderStroke(1.dp, ChloroplastEmerald),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ChloroplastEmerald),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("summary_open_records")
                ) {
                    Text("مشاهده کارنامه و مرور خطاها")
                }

                TextButton(
                    onClick = onBackToSetup,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("بازگشت به انتخاب حالت آزمون")
                }
            }
        }
    }
}

@Composable
private fun SummaryMetric(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
