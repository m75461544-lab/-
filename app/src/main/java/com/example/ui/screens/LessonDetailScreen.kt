package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Spellcheck
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Lesson
import com.example.model.StudyProgressState
import com.example.model.VerseOrLine
import com.example.ui.components.StaggeredAppear
import com.example.ui.components.StudyTimerBannerCard
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.GanjinehColors
import com.example.ui.viewmodel.TimerSessionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LessonDetailScreen(
    lesson: Lesson,
    progressState: StudyProgressState,
    timerState: TimerSessionState,
    onBack: () -> Unit,
    onStartOrResumeTimer: (Lesson) -> Unit,
    onTogglePauseTimer: () -> Unit,
    onFinishStudySession: (Lesson) -> Unit,
    onSimulate30Min: (Lesson) -> Unit,
    onToggleLineRead: (String) -> Unit,
    onStartMemorizeDrill: (String) -> Unit,
    onStartCustomQuiz: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(lesson.id) {
        onStartOrResumeTimer(lesson)
    }

    val tabs = remember(lesson.id) {
        buildList {
            add("متن و تحلیل ابیات")
            add("قلمروها و کارگاه")
            add("واژگان و املا")
            if (lesson.wisdomReading != null) {
                add(lesson.wisdomReading.typeLabel)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "${lesson.numberLabel.toPersianDigits()}: ${lesson.title}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${lesson.author} • ${lesson.formType}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("lesson_detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "بازگشت"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.widthIn(max = 820.dp)) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    StudyTimerBannerCard(
                        timerState = if (timerState.activeLessonId == lesson.id) {
                            timerState
                        } else {
                            TimerSessionState(
                                activeLessonId = lesson.id,
                                activeLessonTitle = lesson.title
                            )
                        },
                        onTogglePlayPause = {
                            if (timerState.activeLessonId == lesson.id) {
                                onTogglePauseTimer()
                            } else {
                                onStartOrResumeTimer(lesson)
                            }
                        },
                        onFinishAndReport = { onFinishStudySession(lesson) },
                        onQuickSimulate30Min = { onSimulate30Min(lesson) }
                    )
                }

                ScrollableTabRow(
                    selectedTabIndex = selectedTab.coerceAtMost(tabs.lastIndex),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = ext.goldAccent,
                    edgePadding = 16.dp
                ) {
                    tabs.forEachIndexed { idx, label ->
                        Tab(
                            selected = selectedTab == idx,
                            onClick = { selectedTab = idx },
                            text = {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            modifier = Modifier.testTag("lesson_tab_$idx")
                        )
                    }
                }

                AnimatedContent(
                    targetState = selectedTab,
                    label = "lesson_detail_tab_content",
                    modifier = Modifier.weight(1f)
                ) { tabIdx ->
                    when (tabIdx) {
                        0 -> {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = ext.poemSurface,
                                        border = BorderStroke(1.dp, ext.cardBorder),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(14.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = lesson.summary,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Button(
                                                    onClick = { onStartCustomQuiz(lesson) },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(48.dp)
                                                        .testTag("lesson_custom_quiz_button"),
                                                    shape = RoundedCornerShape(12.dp)
                                                ) {
                                                    Icon(Icons.Rounded.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                                                    Spacer(Modifier.width(6.dp))
                                                    Text("آزمون همین درس", maxLines = 1)
                                                }
                                                if (lesson.hasMemorizationPoem || lesson.isPoetry) {
                                                    Surface(
                                                        shape = RoundedCornerShape(12.dp),
                                                        color = ext.turquoiseInfo.copy(alpha = 0.16f),
                                                        border = BorderStroke(1.dp, ext.turquoiseInfo.copy(alpha = 0.5f)),
                                                        modifier = Modifier
                                                            .height(48.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .clickable { onStartMemorizeDrill(lesson.id) }
                                                            .testTag("lesson_complete_verse_button")
                                                    ) {
                                                        Box(
                                                            contentAlignment = Alignment.Center,
                                                            modifier = Modifier.padding(horizontal = 12.dp)
                                                        ) {
                                                            Text(
                                                                text = "تکمیل مصرع شعر",
                                                                style = MaterialTheme.typography.labelLarge,
                                                                color = ext.turquoiseInfo
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                itemsIndexed(lesson.lines, key = { _, l -> l.id }) { idx, line ->
                                    StaggeredAppear(index = idx) {
                                        VerseAnalysisCard(
                                            index = idx + 1,
                                            line = line,
                                            isRead = line.id in progressState.readLineIds,
                                            onToggleRead = { onToggleLineRead(line.id) }
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    RealmHighlightCard(
                                        title = "قلمرو زبانی (دستور و واژگان)",
                                        items = lesson.linguisticHighlights,
                                        accent = ext.goldAccent
                                    )
                                }
                                item {
                                    RealmHighlightCard(
                                        title = "قلمرو ادبی (آرایه‌ها و زیبایی‌ها)",
                                        items = lesson.literaryHighlights,
                                        accent = ext.turquoiseInfo
                                    )
                                }
                                item {
                                    RealmHighlightCard(
                                        title = "قلمرو فکری (پیام و قرابت معنایی)",
                                        items = lesson.conceptualHighlights,
                                        accent = ext.emeraldSuccess
                                    )
                                }
                                item {
                                    Text(
                                        text = "حل کارگاه درس‌پژوهی",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = ext.goldAccent,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                                itemsIndexed(lesson.workshopQAs) { idx, qa ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(1.dp, ext.cardBorder),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(14.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Surface(
                                                color = ext.goldAccent.copy(alpha = 0.16f),
                                                shape = RoundedCornerShape(50)
                                            ) {
                                                Text(
                                                    text = qa.realm,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = ext.goldAccent,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                )
                                            }
                                            Text(
                                                text = "پرسش ${(idx + 1).toPersianDigits()}: ${qa.question}",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "پاسخ: ${qa.answer}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        2 -> {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(18.dp),
                                        color = ext.cardElevated,
                                        border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.5f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(14.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Spellcheck,
                                                    contentDescription = null,
                                                    tint = ext.goldAccent
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Text(
                                                    text = "واژگان مهم املایی و تله‌های نوشتاری",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = ext.goldAccent
                                                )
                                            }
                                            FlowRow(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                lesson.spellingTraps.forEach { trap ->
                                                    Surface(
                                                        shape = RoundedCornerShape(10.dp),
                                                        color = MaterialTheme.colorScheme.surface,
                                                        border = BorderStroke(1.dp, ext.cardBorder)
                                                    ) {
                                                        Text(
                                                            text = trap,
                                                            style = MaterialTheme.typography.labelLarge,
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                itemsIndexed(lesson.keyVocabulary) { _, vocab ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(1.dp, ext.cardBorder),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(14.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = vocab.word,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = ext.goldAccent
                                            )
                                            Text(
                                                text = "معنی: ${vocab.meaning}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            if (vocab.spellingNote.isNotBlank()) {
                                                Text(
                                                    text = "نکتهٔ املایی: ${vocab.spellingNote}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = ext.turquoiseInfo
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        else -> {
                            val wisdom = lesson.wisdomReading
                            if (wisdom != null) {
                                LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    item {
                                        Surface(
                                            shape = RoundedCornerShape(20.dp),
                                            color = ext.poemSurface,
                                            border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.45f)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp),
                                                verticalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Text(
                                                    text = "${wisdom.typeLabel}: ${wisdom.title}",
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    color = ext.goldAccent
                                                )
                                                Text(
                                                    text = "مأخذ: ${wisdom.source}",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = ext.turquoiseInfo
                                                )
                                                Text(
                                                    text = wisdom.text,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = ext.emeraldSuccess.copy(alpha = 0.14f),
                                                    border = BorderStroke(1.dp, ext.emeraldSuccess.copy(alpha = 0.4f))
                                                ) {
                                                    Text(
                                                        text = "پیام حکمت: ${wisdom.keyMessage}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.padding(12.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    itemsIndexed(wisdom.keyWords) { _, kw ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            border = BorderStroke(1.dp, ext.cardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = kw.word,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = ext.goldAccent
                                                )
                                                Spacer(Modifier.width(12.dp))
                                                Text(
                                                    text = kw.meaning,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurface
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
    }
}

@Composable
private fun VerseAnalysisCard(
    index: Int,
    line: VerseOrLine,
    isRead: Boolean,
    onToggleRead: () -> Unit
) {
    val ext = GanjinehColors.current
    var expanded by rememberSaveable(line.id) { mutableStateOf(index <= 2) }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isRead) ext.cardElevated else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (isRead) ext.emeraldSuccess.copy(alpha = 0.55f) else ext.cardBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ext.goldAccent.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = if (line.isPoetry) "بیت ${index.toPersianDigits()}" else "بند ${index.toPersianDigits()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = ext.goldAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    if (line.isMemorizable) {
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ext.turquoiseInfo.copy(alpha = 0.16f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Bookmark,
                                    contentDescription = null,
                                    tint = ext.turquoiseInfo,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "حفظی",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ext.turquoiseInfo
                                )
                            }
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleRead,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("toggle_read_${line.id}")
                    ) {
                        Icon(
                            imageVector = if (isRead) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                            contentDescription = if (isRead) "خوانده شده" else "علامت‌گذاری به عنوان خوانده‌شده",
                            tint = if (isRead) ext.emeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("expand_line_${line.id}")
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = if (expanded) "بستن تحلیل" else "نمایش تحلیل"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = line.firstHemistich,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (line.secondHemistich.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = line.secondHemistich,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = ext.goldAccent
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ext.poemSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "معنی روان: ${line.meaning}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    if (line.linguisticNote.isNotBlank()) {
                        Text(
                            text = "• قلمرو زبانی: ${line.linguisticNote}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (line.literaryDevices.isNotEmpty()) {
                        Text(
                            text = "• قلمرو ادبی: ${line.literaryDevices.joinToString(" | ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ext.turquoiseInfo
                        )
                    }
                    if (line.conceptualNote.isNotBlank()) {
                        Text(
                            text = "• قلمرو فکری: ${line.conceptualNote}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ext.emeraldSuccess
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RealmHighlightCard(
    title: String,
    items: List<String>,
    accent: androidx.compose.ui.graphics.Color
) {
    val ext = GanjinehColors.current
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = accent
                )
            }
            items.forEach { item ->
                Text(
                    text = "— $item",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
