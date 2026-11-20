package com.example.model

import androidx.compose.runtime.Immutable
import com.example.data.quiz.VerseEvaluationResult

enum class QuizEngineMode(
    val titleFa: String,
    val shortTitleFa: String,
    val subtitleFa: String
) {
    COMPREHENSIVE_LESSONS(
        titleFa = "آزمون جامع از دروس فارسی",
        shortTitleFa = "آزمون جامع دروس",
        subtitleFa = "طراحی خودکار از واژگان، املا، آرایه‌های ادبی، کارگاه درس‌پژوهی، قرابت معنایی و شعر"
    ),
    HEMISTICH_COMPLETION_WRITTEN(
        titleFa = "تکمیل مصرع شعر (نوشتاری و واژه‌چین)",
        shortTitleFa = "تکمیل مصرع نوشتاری",
        subtitleFa = "یک مصرع از بیت نمایش داده می‌شود و مصرع دیگر را می‌نویسید یا با واژه‌ها می‌چینید"
    ),
    HEMISTICH_COMPLETION_CHOICE(
        titleFa = "تکمیل مصرع شعر (چهارگزینه‌ای)",
        shortTitleFa = "تکمیل مصرع تستی",
        subtitleFa = "مصرع جاافتاده را از میان چهار مصرع هم‌تراز و ادبی انتخاب می‌کنید"
    )
}

enum class HemistichBlankTarget(
    val titleFa: String,
    val descriptionFa: String
) {
    SECOND_ONLY(
        titleFa = "مصرع دوم",
        descriptionFa = "مصرع اول نمایش داده می‌شود و مصرع دوم را کامل می‌کنید"
    ),
    FIRST_ONLY(
        titleFa = "مصرع اول (معکوس)",
        descriptionFa = "مصرع دوم نمایش داده می‌شود و مصرع اول را به یاد می‌آورید"
    ),
    MIXED(
        titleFa = "ترکیبی (اول و دوم)",
        descriptionFa = "به صورت تصادفی مصرع اول یا دوم هر بیت پنهان می‌شود"
    )
}

enum class LessonPoolScope(
    val titleFa: String
) {
    ALL_LESSONS("همهٔ درس‌ها"),
    MEMORIZATION_ONLY("فقط شعرهای حفظی"),
    SELECTED_LESSONS("انتخاب دستی درس‌ها")
}

@Immutable
data class QuizGenerationConfig(
    val mode: QuizEngineMode = QuizEngineMode.COMPREHENSIVE_LESSONS,
    val scope: LessonPoolScope = LessonPoolScope.ALL_LESSONS,
    val selectedLessonIds: Set<String> = emptySet(),
    val questionCount: Int = 8,
    val hemistichBlankTarget: HemistichBlankTarget = HemistichBlankTarget.SECOND_ONLY,
    val includeMemorizedOnlyInPoetry: Boolean = true,
    val includeVocabAndSpelling: Boolean = true,
    val includeWorkshopAndWisdom: Boolean = true,
    val includeDevicesAndMeaning: Boolean = true,
    val includeHemistichInComprehensive: Boolean = true
)

@Immutable
data class QuizSessionState(
    val title: String = "",
    val subtitle: String = "",
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val verseUserInput: String = "",
    val hasSubmittedCurrent: Boolean = false,
    val currentIsCorrect: Boolean = false,
    val currentFeedback: String = "",
    val lastVerseEval: VerseEvaluationResult? = null,
    val revealedHintWordCount: Int = 0,
    val correctCount: Int = 0,
    val isFinished: Boolean = false
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)
}
