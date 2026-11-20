package com.example.model

import androidx.compose.runtime.Immutable

enum class AppThemeMode(
    val titleFa: String,
    val subtitleFa: String,
    val isDark: Boolean
) {
    DARK_BROWN(
        titleFa = "قهوه‌ای تیره (پیش‌فرض)",
        subtitleFa = "طراحی اصیل کهن‌نامه با درخشش طلایی",
        isDark = true
    ),
    MIDNIGHT_INDIGO(
        titleFa = "سرمه‌ای شبِ شیراز",
        subtitleFa = "آرامش شبانه با فیروزه‌ای و زرّین",
        isDark = true
    ),
    EMERALD_GARDEN(
        titleFa = "سبز زمردی باغ نظر",
        subtitleFa = "الهام از کاشی‌کاری ایرانی و سرو",
        isDark = true
    ),
    PARCHMENT_LIGHT(
        titleFa = "کاغذی روشن (نسخهٔ خطی)",
        subtitleFa = "خوانایی بالا در نور روز روی کاغذ گرم",
        isDark = false
    )
}

enum class LessonCategory(val titleFa: String) {
    ALL("همهٔ درس‌ها"),
    MEMORIZE("شعرهای حفظی"),
    POETRY("نظم و شعر"),
    PROSE("نثر کهن و معاصر")
}

@Immutable
data class VerseOrLine(
    val id: String,
    val firstHemistich: String,
    val secondHemistich: String = "",
    val isPoetry: Boolean = true,
    val isMemorizable: Boolean = false,
    val meaning: String,
    val linguisticNote: String = "",
    val literaryDevices: List<String> = emptyList(),
    val conceptualNote: String = ""
)

@Immutable
data class VocabularyItem(
    val word: String,
    val meaning: String,
    val spellingNote: String = ""
)

@Immutable
data class WorkshopQA(
    val realm: String,
    val question: String,
    val answer: String
)

@Immutable
data class WisdomReading(
    val title: String,
    val source: String,
    val typeLabel: String,
    val text: String,
    val keyMessage: String,
    val keyWords: List<VocabularyItem> = emptyList()
)

@Immutable
data class Lesson(
    val id: String,
    val numberLabel: String,
    val title: String,
    val author: String,
    val chapterTheme: String,
    val formType: String,
    val isPoetry: Boolean,
    val hasMemorizationPoem: Boolean,
    val summary: String,
    val lines: List<VerseOrLine>,
    val keyVocabulary: List<VocabularyItem>,
    val spellingTraps: List<String>,
    val linguisticHighlights: List<String>,
    val literaryHighlights: List<String>,
    val conceptualHighlights: List<String>,
    val workshopQAs: List<WorkshopQA>,
    val wisdomReading: WisdomReading? = null
)

enum class GrammarCategory(val titleFa: String) {
    ALL("همهٔ مباحث"),
    GRAMMAR("دستور زبان"),
    LITERARY_DEVICES("آرایه‌های ادبی"),
    SEMANTICS_SPELLING("روابط معنایی و واژگان")
}

@Immutable
data class ComparisonTable(
    val title: String,
    val headers: List<String>,
    val rows: List<List<String>>
)

@Immutable
data class GrammarDeviceTopic(
    val id: String,
    val title: String,
    val category: GrammarCategory,
    val lessonRef: String,
    val summary: String,
    val formulaOrStructure: String,
    val keyRules: List<String>,
    val examples: List<Pair<String, String>>,
    val examTip: String,
    val tables: List<ComparisonTable> = emptyList()
)

enum class QuestionDifficulty(val titleFa: String) {
    STANDARD("سطح کتاب"),
    ADVANCED("پیشرفته (تستی)"),
    OLYMPIAD("فوق‌پیشرفته و ترکیبی")
}

enum class QuestionDomain(val titleFa: String) {
    LINGUISTIC("قلمرو زبانی و دستور"),
    LITERARY("قلمرو ادبی و آرایه"),
    CONCEPTUAL("قلمرو فکری و قرابت"),
    SPELLING_VOCAB("املا و روابط معنایی"),
    MEMORIZATION("تکمیل مصرع و شعر حفظی")
}

enum class QuestionType {
    MULTIPLE_CHOICE,
    VERSE_COMPLETION
}

@Immutable
data class QuizQuestion(
    val id: String,
    val type: QuestionType,
    val domain: QuestionDomain,
    val difficulty: QuestionDifficulty,
    val lessonId: String,
    val lessonTitle: String,
    val prompt: String,
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0,
    val expectedVerseText: String = "",
    val acceptableKeywords: List<String> = emptyList(),
    val wordHints: List<String> = emptyList(),
    val explanation: String,
    val firstHemistich: String = "",
    val secondHemistich: String = "",
    val blankFirstHemistich: Boolean = false
)

@Immutable
data class StudySessionReport(
    val lessonId: String,
    val lessonTitle: String,
    val sessionSeconds: Int,
    val totalLessonSeconds: Int,
    val targetSeconds: Int,
    val sectionsCoveredCount: Int,
    val totalSectionsCount: Int,
    val reachedThirtyMinGoal: Boolean,
    val generatedQuestions: List<QuizQuestion>
)

@Immutable
data class StudyProgressState(
    val themeMode: AppThemeMode = AppThemeMode.DARK_BROWN,
    val lessonStudySeconds: Map<String, Int> = emptyMap(),
    val completedLessonIds: Set<String> = emptySet(),
    val readLineIds: Set<String> = emptySet(),
    val totalQuizzesCompleted: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val bestScorePercent: Int = 0,
    val totalStudySessions: Int = 0
)
