package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Lesson
import com.example.model.LessonCategory
import com.example.model.StudyProgressState
import com.example.ui.components.PersianHeroBanner
import com.example.ui.components.ShamsaMedallionCanvas
import com.example.ui.components.StaggeredAppear
import com.example.ui.components.formatDurationPersian
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.GanjinehColors
import com.example.ui.viewmodel.TimerSessionState

@Composable
fun HomeLessonsScreen(
    lessons: List<Lesson>,
    searchQuery: String,
    selectedCategory: LessonCategory,
    progressState: StudyProgressState,
    timerState: TimerSessionState,
    onSearchChange: (String) -> Unit,
    onSelectCategory: (LessonCategory) -> Unit,
    onOpenLesson: (String) -> Unit,
    onNavigateToGrammar: () -> Unit,
    onNavigateToBank: () -> Unit,
    onStartMemorizeDrill: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    val filteredLessons = remember(lessons, searchQuery, selectedCategory) {
        lessons.filter { lesson ->
            val matchesCat = when (selectedCategory) {
                LessonCategory.ALL -> true
                LessonCategory.MEMORIZE -> lesson.hasMemorizationPoem
                LessonCategory.POETRY -> lesson.isPoetry
                LessonCategory.PROSE -> !lesson.isPoetry
            }
            val q = searchQuery.trim()
            val matchesQuery = q.isEmpty() ||
                lesson.title.contains(q) ||
                lesson.numberLabel.contains(q) ||
                lesson.author.contains(q) ||
                lesson.chapterTheme.contains(q)
            matchesCat && matchesQuery
        }
    }

    val totalStudySecs = progressState.lessonStudySeconds.values.sum()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 700.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_lessons_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.widthIn(max = 840.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PersianHeroBanner(
                            title = "گنجینهٔ فارسی پایهٔ دهم",
                            subtitle = "جزوهٔ جامع دروس، دستور زبان، آرایه‌ها و بانک سوال پیشرفته",
                            badgeText = "مطالعهٔ ۳۰ دقیقه‌ای • آزمون هوشمند",
                            trailingContent = {
                                ShamsaMedallionCanvas(modifier = Modifier.size(64.dp))
                            }
                        )

                        // Three main pillars quick cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PillarShortcutCard(
                                title = "۱. مطالب دروس",
                                subtitle = "${lessons.size.toPersianDigits()} فصل کامل",
                                icon = Icons.AutoMirrored.Rounded.MenuBook,
                                highlighted = true,
                                onClick = {},
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pillar_lessons_card")
                            )
                            PillarShortcutCard(
                                title = "۲. دستور و آرایه",
                                subtitle = "آموزش جامع",
                                icon = Icons.Rounded.Psychology,
                                highlighted = false,
                                onClick = onNavigateToGrammar,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pillar_grammar_card")
                            )
                            PillarShortcutCard(
                                title = "۳. بانک پیشرفته",
                                subtitle = "۶۰+ تست کنکوری",
                                icon = Icons.Rounded.Quiz,
                                highlighted = false,
                                onClick = onNavigateToBank,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pillar_bank_card")
                            )
                        }

                        // Active timer or memorization quick callout
                        AnimatedVisibility(visible = timerState.activeLessonId != null) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = ext.goldAccent.copy(alpha = 0.14f),
                                border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        timerState.activeLessonId?.let(onOpenLesson)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.Timer,
                                            contentDescription = null,
                                            tint = ext.goldAccent
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "جلسهٔ مطالعهٔ فعال: ${timerState.activeLessonTitle}",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "زمان سپری‌شده: ${formatDurationPersian(timerState.elapsedSeconds)} از ۳۰:۰۰",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ext.goldAccent
                                            )
                                        }
                                    }
                                    Text(
                                        text = "ادامه",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = ext.goldAccent
                                    )
                                }
                            }
                        }

                        // Summary stats + quick memorization verse drill chip
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, ext.cardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "مجموع مطالعه: ${formatDurationPersian(totalStudySecs)} | دروس تکمیل‌شده: ${progressState.completedLessonIds.size.toPersianDigits()}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "تمرین تکمیل مصرع دوم شعرهای حفظی",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = ext.turquoiseInfo.copy(alpha = 0.18f),
                                    border = BorderStroke(1.dp, ext.turquoiseInfo.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable(onClick = onStartMemorizeDrill)
                                        .testTag("home_memorize_drill_button")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AutoStories,
                                            contentDescription = null,
                                            tint = ext.turquoiseInfo,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "آزمون حفظیات",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = ext.turquoiseInfo
                                        )
                                    }
                                }
                            }
                        }

                        // Search bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("lesson_search_input"),
                            placeholder = {
                                Text(
                                    text = "جست‌وجو در عنوان درس، شاعر یا نویسنده...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "جست‌وجو",
                                    tint = ext.goldAccent
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchChange("") }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = "پاک کردن جست‌وجو"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ext.goldAccent,
                                unfocusedBorderColor = ext.cardBorder,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )

                        // Filter chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LessonCategory.entries.forEach { category ->
                                val selected = category == selectedCategory
                                FilterChip(
                                    selected = selected,
                                    onClick = { onSelectCategory(category) },
                                    label = {
                                        Text(
                                            text = category.titleFa,
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    modifier = Modifier.testTag("lesson_filter_${category.name.lowercase()}")
                                )
                            }
                        }
                    }
                }
            }

            if (isWide) {
                val pairs = filteredLessons.chunked(2)
                itemsIndexed(pairs, key = { _, pair -> pair.first().id }) { idx, pair ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.widthIn(max = 840.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            pair.forEach { lesson ->
                                Box(modifier = Modifier.weight(1f)) {
                                    StaggeredAppear(index = idx) {
                                        LessonCardItem(
                                            lesson = lesson,
                                            progressState = progressState,
                                            onClick = { onOpenLesson(lesson.id) }
                                        )
                                    }
                                }
                            }
                            if (pair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                itemsIndexed(filteredLessons, key = { _, item -> item.id }) { idx, lesson ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.widthIn(max = 640.dp)) {
                            StaggeredAppear(index = idx) {
                                LessonCardItem(
                                    lesson = lesson,
                                    progressState = progressState,
                                    onClick = { onOpenLesson(lesson.id) }
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PillarShortcutCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    highlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = GanjinehColors.current
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (highlighted) ext.cardElevated else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (highlighted) ext.goldAccent.copy(alpha = 0.65f) else ext.cardBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (highlighted) ext.goldAccent else ext.turquoiseInfo,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LessonCardItem(
    lesson: Lesson,
    progressState: StudyProgressState,
    onClick: () -> Unit
) {
    val ext = GanjinehColors.current
    val readCount = lesson.lines.count { it.id in progressState.readLineIds }
    val totalLines = lesson.lines.size.coerceAtLeast(1)
    val progress = (readCount.toFloat() / totalLines).coerceIn(0f, 1f)
    val studiedSecs = progressState.lessonStudySeconds[lesson.id] ?: 0
    val isCompleted = lesson.id in progressState.completedLessonIds

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("lesson_card_${lesson.id}"),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (isCompleted) ext.emeraldSuccess.copy(alpha = 0.55f) else ext.cardBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ext.goldAccent.copy(alpha = 0.16f),
                        border = BorderStroke(1.dp, ext.goldAccent.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = lesson.numberLabel.toPersianDigits(),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = ext.goldAccent,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${lesson.author} • ${lesson.formType}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (lesson.hasMemorizationPoem) {
                        Surface(
                            shape = CircleShape,
                            color = ext.turquoiseInfo.copy(alpha = 0.16f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.BookmarkAdded,
                                contentDescription = "دارای شعر حفظی",
                                tint = ext.turquoiseInfo,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(16.dp)
                            )
                        }
                    }
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = "تکمیل شده",
                            tint = ext.emeraldSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Text(
                text = lesson.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "پیشرفت مطالعه: ${readCount.toPersianDigits()} از ${totalLines.toPersianDigits()} بخش",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (studiedSecs > 0) {
                            "زمان ثبت‌شده: ${formatDurationPersian(studiedSecs)}"
                        } else if (lesson.hasMemorizationPoem) {
                            "شعر حفظی"
                        } else {
                            lesson.chapterTheme
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (studiedSecs > 0) ext.goldAccent else ext.turquoiseInfo,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = if (isCompleted) ext.emeraldSuccess else ext.goldAccent,
                    trackColor = ext.cardBorder.copy(alpha = 0.35f)
                )
            }
        }
    }
}
