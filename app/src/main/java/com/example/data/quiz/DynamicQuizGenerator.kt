package com.example.data.quiz

import com.example.model.HemistichBlankTarget
import com.example.model.Lesson
import com.example.model.LessonPoolScope
import com.example.model.QuizEngineMode
import com.example.model.QuizGenerationConfig
import com.example.model.QuizQuestion
import com.example.model.VerseOrLine
import kotlin.math.max
import kotlin.random.Random

data class VerseEvaluationResult(
    val isCorrect: Boolean,
    val similarityPercent: Int,
    val feedback: String,
    val matchedWords: Int = 0,
    val totalExpectedWords: Int = 0,
    val missingWords: List<String> = emptyList()
)

object DynamicQuizGenerator {

    /**
     * Normalizes Persian text for resilient, fair poem-completion grading:
     * - Converts Arabic Yeh/Kaf/Teh-Marbuta to standard Persian letters
     * - Strips optional Arabic diacritics & Tatweel
     * - Treats half-space (\u200C) and spaces uniformly so minor spacing differences don't unfairly fail,
     *   while genuinely misspelled letters (e.g. ص/س/ث، ق/غ، ط/ت) remain distinct!
     */
    fun normalizePersianForMatching(text: String): String {
        val sb = StringBuilder(text.length)
        for (ch in text) {
            when (ch) {
                'ي', 'ى', 'ئ' -> if (ch == 'ئ') sb.append('ئ') else sb.append('ی')
                'ك' -> sb.append('ک')
                'أ', 'إ', 'آ' -> sb.append(if (ch == 'آ') 'آ' else 'ا')
                'ة' -> sb.append('ه')
                '\u200C', '\u200F', '\u200E' -> sb.append(' ')
                // Strip diacritics (harakat + tanvin + tashdid)
                '\u064B', '\u064C', '\u064D', '\u064E', '\u064F', '\u0650', '\u0651', '\u0652', '\u0670', '\u0640' -> Unit
                '،', '؛', '.', '؟', '!', ':', '«', '»', '(', ')', '-', '—', '/', '\n', '\t' -> sb.append(' ')
                else -> sb.append(ch)
            }
        }
        return sb.toString()
            .trim()
            .replace(Regex("\\s+"), " ")
    }

    fun evaluateVerseCompletion(userInput: String, expectedVerse: String): VerseEvaluationResult {
        val normUser = normalizePersianForMatching(userInput)
        val normExpected = normalizePersianForMatching(expectedVerse)

        val rawExpectedWords = expectedVerse.split(Regex("\\s+")).filter { it.isNotBlank() }
        val expectedWords = normExpected.split(" ").filter { it.isNotBlank() }

        if (normUser.isBlank()) {
            return VerseEvaluationResult(
                isCorrect = false,
                similarityPercent = 0,
                feedback = "پاسخی وارد نشده است. مصرع جاافتاده را بنویسید یا از واژه‌چین کمک بگیرید.",
                matchedWords = 0,
                totalExpectedWords = expectedWords.size,
                missingWords = rawExpectedWords
            )
        }

        if (normUser == normExpected) {
            return VerseEvaluationResult(
                isCorrect = true,
                similarityPercent = 100,
                feedback = "آفرین! مصرع را کاملاً دقیق و بدون غلط املایی تکمیل کردید.",
                matchedWords = expectedWords.size,
                totalExpectedWords = expectedWords.size,
                missingWords = emptyList()
            )
        }

        val distance = levenshteinDistance(normUser, normExpected)
        val maxLen = max(normUser.length, normExpected.length).coerceAtLeast(1)
        val similarity = (((maxLen - distance).toFloat() / maxLen) * 100).toInt().coerceIn(0, 100)

        val userWords = normUser.split(" ").filter { it.isNotBlank() }
        val matchedWordCount = expectedWords.count { it in userWords }
        val wordOverlap = if (expectedWords.isNotEmpty()) {
            (matchedWordCount * 100) / expectedWords.size
        } else 0
        val missingWords = rawExpectedWords.filter { rawWord ->
            val normWord = normalizePersianForMatching(rawWord)
            normWord.isNotBlank() && normWord !in userWords
        }

        return when {
            similarity >= 88 && wordOverlap >= 80 -> VerseEvaluationResult(
                isCorrect = true,
                similarityPercent = similarity,
                feedback = "بسیار عالی! مصرع با دقت بسیار بالا ($similarity٪) تکمیل شد.",
                matchedWords = matchedWordCount,
                totalExpectedWords = expectedWords.size,
                missingWords = missingWords
            )
            similarity >= 65 -> VerseEvaluationResult(
                isCorrect = false,
                similarityPercent = similarity,
                feedback = "نزدیک بودید ($similarity٪ تطابق)، اما برخی واژگان یا حروف املایی با متن اصلی متفاوت است. متن دقیق را ببینید.",
                matchedWords = matchedWordCount,
                totalExpectedWords = expectedWords.size,
                missingWords = missingWords
            )
            else -> VerseEvaluationResult(
                isCorrect = false,
                similarityPercent = similarity,
                feedback = "نیاز به مرور بیشتر دارد ($similarity٪ تطابق). مصرع صحیح را با دقت مطالعه کنید.",
                matchedWords = matchedWordCount,
                totalExpectedWords = expectedWords.size,
                missingWords = missingWords
            )
        }
    }

    private fun levenshteinDistance(a: String, b: String): Int {
        if (a == b) return 0
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length
        var prev = IntArray(b.length + 1) { it }
        var curr = IntArray(b.length + 1)
        for (i in 1..a.length) {
            curr[0] = i
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                curr[j] = minOf(curr[j - 1] + 1, prev[j] + 1, prev[j - 1] + cost)
            }
            val tmp = prev
            prev = curr
            curr = tmp
        }
        return prev[b.length]
    }

    /**
     * Main Quiz Generation Engine pulling directly from Persian literature lessons
     * with dedicated modes for written & multiple-choice hemistich completion.
     */
    fun generateQuiz(
        config: QuizGenerationConfig,
        allLessons: List<Lesson>,
        seed: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    ): List<QuizQuestion> {
        val rng = Random(seed)
        val count = config.questionCount.coerceIn(1, 30)
        val scopedLessons = resolveScopedLessons(config, allLessons)

        return when (config.mode) {
            QuizEngineMode.HEMISTICH_COMPLETION_WRITTEN -> generateHemistichQuiz(
                scopedLessons = scopedLessons,
                allLessons = allLessons,
                count = count,
                blankTarget = config.hemistichBlankTarget,
                memorizedOnlyPreferred = config.includeMemorizedOnlyInPoetry,
                writtenMode = true,
                rng = rng
            )

            QuizEngineMode.HEMISTICH_COMPLETION_CHOICE -> generateHemistichQuiz(
                scopedLessons = scopedLessons,
                allLessons = allLessons,
                count = count,
                blankTarget = config.hemistichBlankTarget,
                memorizedOnlyPreferred = config.includeMemorizedOnlyInPoetry,
                writtenMode = false,
                rng = rng
            )

            QuizEngineMode.COMPREHENSIVE_LESSONS -> generateComprehensiveQuiz(
                config = config,
                scopedLessons = scopedLessons,
                allLessons = allLessons,
                count = count,
                rng = rng
            )
        }
    }

    private fun resolveScopedLessons(
        config: QuizGenerationConfig,
        allLessons: List<Lesson>
    ): List<Lesson> = when (config.scope) {
        LessonPoolScope.ALL_LESSONS -> allLessons
        LessonPoolScope.MEMORIZATION_ONLY -> allLessons.filter { it.hasMemorizationPoem }.ifEmpty { allLessons }
        LessonPoolScope.SELECTED_LESSONS -> {
            if (config.selectedLessonIds.isEmpty()) {
                allLessons
            } else {
                allLessons.filter { it.id in config.selectedLessonIds }.ifEmpty { allLessons }
            }
        }
    }

    private fun collectPoetryPairs(
        scopedLessons: List<Lesson>,
        allLessons: List<Lesson>,
        memorizedOnlyPreferred: Boolean
    ): List<Pair<Lesson, VerseOrLine>> {
        val fromScoped = scopedLessons.flatMap { lesson ->
            val poetry = lesson.lines.filter { it.isPoetry && it.firstHemistich.isNotBlank() && it.secondHemistich.isNotBlank() }
            val chosen = if (memorizedOnlyPreferred) {
                poetry.filter { it.isMemorizable }.ifEmpty { poetry }
            } else {
                poetry
            }
            chosen.map { lesson to it }
        }
        if (fromScoped.isNotEmpty()) return fromScoped

        // Fallback if user selected purely prose lessons while launching dedicated poetry mode
        return allLessons.flatMap { lesson ->
            val poetry = lesson.lines.filter { it.isPoetry && it.firstHemistich.isNotBlank() && it.secondHemistich.isNotBlank() }
            val chosen = if (memorizedOnlyPreferred) {
                poetry.filter { it.isMemorizable }.ifEmpty { poetry }
            } else {
                poetry
            }
            chosen.map { lesson to it }
        }
    }

    private fun generateHemistichQuiz(
        scopedLessons: List<Lesson>,
        allLessons: List<Lesson>,
        count: Int,
        blankTarget: HemistichBlankTarget,
        memorizedOnlyPreferred: Boolean,
        writtenMode: Boolean,
        rng: Random
    ): List<QuizQuestion> {
        val pairs = collectPoetryPairs(scopedLessons, allLessons, memorizedOnlyPreferred)
            .shuffled(rng)

        val generated = mutableListOf<QuizQuestion>()
        for ((idx, pair) in pairs.withIndex()) {
            if (generated.size >= count) break
            val (lesson, line) = pair
            val question = if (writtenMode) {
                LessonQuestionExtractors.buildWrittenHemistichQuestion(
                    lesson = lesson,
                    line = line,
                    blankTarget = blankTarget,
                    rng = rng,
                    index = idx
                )
            } else {
                LessonQuestionExtractors.buildChoiceHemistichQuestion(
                    lesson = lesson,
                    line = line,
                    blankTarget = blankTarget,
                    allLessons = allLessons,
                    rng = rng,
                    index = idx
                )
            }
            if (question != null) {
                generated += question
            }
        }
        return generated
    }

    private fun generateComprehensiveQuiz(
        config: QuizGenerationConfig,
        scopedLessons: List<Lesson>,
        allLessons: List<Lesson>,
        count: Int,
        rng: Random
    ): List<QuizQuestion> {
        val buckets = mutableListOf<MutableList<QuizQuestion>>()

        // 1. Poetry hemistich bucket (written + choice blend)
        if (config.includeHemistichInComprehensive) {
            val poetryBucket = mutableListOf<QuizQuestion>()
            val poetryPairs = scopedLessons.flatMap { lesson ->
                val poetry = lesson.lines.filter { it.isPoetry && it.secondHemistich.isNotBlank() }
                val preferred = poetry.filter { it.isMemorizable }.ifEmpty { poetry }
                preferred.map { lesson to it }
            }.shuffled(rng)

            poetryPairs.take((count / 2).coerceAtLeast(2)).forEachIndexed { idx, (lesson, line) ->
                val q = if (idx % 3 == 2) {
                    LessonQuestionExtractors.buildChoiceHemistichQuestion(
                        lesson = lesson,
                        line = line,
                        blankTarget = config.hemistichBlankTarget,
                        allLessons = allLessons,
                        rng = rng,
                        index = idx
                    )
                } else {
                    LessonQuestionExtractors.buildWrittenHemistichQuestion(
                        lesson = lesson,
                        line = line,
                        blankTarget = config.hemistichBlankTarget,
                        rng = rng,
                        index = idx
                    )
                }
                if (q != null) poetryBucket += q
            }
            if (poetryBucket.isEmpty() && scopedLessons.size == 1) {
                // Single prose lesson: add 1 bonus memorization couplet so user still practices poetry
                val backupLesson = allLessons.firstOrNull {
                    it.hasMemorizationPoem && it.lines.any { l -> l.isPoetry && l.secondHemistich.isNotBlank() }
                }
                val backupLine = backupLesson?.lines
                    ?.filter { it.isPoetry && it.secondHemistich.isNotBlank() }
                    ?.randomOrNull(rng)
                if (backupLesson != null && backupLine != null) {
                    poetryBucket += LessonQuestionExtractors.buildWrittenHemistichQuestion(
                        lesson = backupLesson,
                        line = backupLine,
                        blankTarget = config.hemistichBlankTarget,
                        rng = rng,
                        index = 0,
                        lessonLabelPrefix = "مرور شعر حفظی"
                    )
                }
            }
            if (poetryBucket.isNotEmpty()) buckets += poetryBucket
        }

        // 2. Vocabulary & Spelling bucket
        if (config.includeVocabAndSpelling) {
            val vocabBucket = mutableListOf<QuizQuestion>()
            scopedLessons.shuffled(rng).forEach { lesson ->
                lesson.keyVocabulary.shuffled(rng).take(2).forEachIndexed { idx, vocab ->
                    LessonQuestionExtractors.buildVocabularyQuestion(lesson, vocab, allLessons, rng, idx)
                        ?.let { vocabBucket += it }
                }
                lesson.keyVocabulary.filter { it.spellingNote.isNotBlank() }.shuffled(rng).take(1).forEachIndexed { idx, vocab ->
                    LessonQuestionExtractors.buildSpellingNoteQuestion(lesson, vocab, allLessons, rng, idx)
                        ?.let { vocabBucket += it }
                }
            }
            vocabBucket.shuffle(rng)
            if (vocabBucket.isNotEmpty()) buckets += vocabBucket
        }

        // 3. Literary devices & conceptual line analysis bucket
        if (config.includeDevicesAndMeaning) {
            val literaryBucket = mutableListOf<QuizQuestion>()
            scopedLessons.shuffled(rng).forEach { lesson ->
                lesson.lines.filter { it.literaryDevices.isNotEmpty() }.shuffled(rng).take(2).forEach { line ->
                    LessonQuestionExtractors.buildLiteraryDeviceQuestion(lesson, line, allLessons, rng)
                        ?.let { literaryBucket += it }
                }
                lesson.lines.filter { it.conceptualNote.length >= 18 }.shuffled(rng).take(2).forEach { line ->
                    LessonQuestionExtractors.buildLineConceptualQuestion(lesson, line, allLessons, rng)
                        ?.let { literaryBucket += it }
                }
            }
            literaryBucket.shuffle(rng)
            if (literaryBucket.isNotEmpty()) buckets += literaryBucket
        }

        // 4. Workshop Q&As & Wisdom Readings bucket
        if (config.includeWorkshopAndWisdom) {
            val workshopBucket = mutableListOf<QuizQuestion>()
            scopedLessons.shuffled(rng).forEach { lesson ->
                lesson.workshopQAs.shuffled(rng).take(2).forEachIndexed { idx, qa ->
                    LessonQuestionExtractors.buildWorkshopQuestion(lesson, qa, allLessons, rng, idx)
                        ?.let { workshopBucket += it }
                }
                LessonQuestionExtractors.buildWisdomReadingQuestion(lesson, allLessons, rng)
                    ?.let { workshopBucket += it }
            }
            workshopBucket.shuffle(rng)
            if (workshopBucket.isNotEmpty()) buckets += workshopBucket
        }

        // 5. Curated Advanced Bank questions matching scoped lessons
        val scopedIds = scopedLessons.map { it.id }.toSet()
        val bankMatches = AdvancedQuestionBank.allQuestions
            .filter { it.lessonId in scopedIds }
            .shuffled(rng)
            .toMutableList()
        if (bankMatches.isNotEmpty()) buckets += bankMatches

        // Round-robin merge from buckets for maximum topic diversity
        val result = mutableListOf<QuizQuestion>()
        val seenPrompts = mutableSetOf<String>()
        var round = 0
        while (result.size < count && buckets.any { round < it.size }) {
            for (bucket in buckets) {
                if (result.size >= count) break
                val candidate = bucket.getOrNull(round) ?: continue
                if (seenPrompts.add(candidate.prompt)) {
                    result += candidate
                }
            }
            round++
        }

        // Final fallback from full bank if still short
        if (result.size < count) {
            for (candidate in AdvancedQuestionBank.allQuestions.shuffled(rng)) {
                if (result.size >= count) break
                if (seenPrompts.add(candidate.prompt)) {
                    result += candidate
                }
            }
        }

        return result.take(count)
    }

    /**
     * Generates a rich, tailored quiz directly from the lesson the user just studied.
     * - Prioritizes interactive half-verse completion (تکمیل مصرع شعر) if the lesson has poetry.
     * - Combines vocabulary, spelling, workshop Q&As, literary devices, conceptual analysis, and curated questions.
     */
    fun generatePostStudyQuiz(
        lesson: Lesson,
        allLessons: List<Lesson>,
        questionCount: Int = 7,
        seed: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    ): List<QuizQuestion> = generateQuiz(
        config = QuizGenerationConfig(
            mode = QuizEngineMode.COMPREHENSIVE_LESSONS,
            scope = LessonPoolScope.SELECTED_LESSONS,
            selectedLessonIds = setOf(lesson.id),
            questionCount = questionCount,
            hemistichBlankTarget = HemistichBlankTarget.SECOND_ONLY
        ),
        allLessons = allLessons,
        seed = seed
    )

    fun generateMemorizationCompletionDrill(
        lessons: List<Lesson>,
        targetLessonId: String? = null,
        count: Int = 8,
        blankTarget: HemistichBlankTarget = HemistichBlankTarget.SECOND_ONLY,
        writtenMode: Boolean = true,
        seed: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    ): List<QuizQuestion> {
        val config = if (targetLessonId != null) {
            QuizGenerationConfig(
                mode = if (writtenMode) QuizEngineMode.HEMISTICH_COMPLETION_WRITTEN else QuizEngineMode.HEMISTICH_COMPLETION_CHOICE,
                scope = LessonPoolScope.SELECTED_LESSONS,
                selectedLessonIds = setOf(targetLessonId),
                questionCount = count,
                hemistichBlankTarget = blankTarget
            )
        } else {
            QuizGenerationConfig(
                mode = if (writtenMode) QuizEngineMode.HEMISTICH_COMPLETION_WRITTEN else QuizEngineMode.HEMISTICH_COMPLETION_CHOICE,
                scope = LessonPoolScope.MEMORIZATION_ONLY,
                questionCount = count,
                hemistichBlankTarget = blankTarget
            )
        }
        return generateQuiz(config = config, allLessons = lessons, seed = seed)
    }
}
