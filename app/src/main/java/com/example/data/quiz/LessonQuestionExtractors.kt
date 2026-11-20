package com.example.data.quiz

import com.example.model.HemistichBlankTarget
import com.example.model.Lesson
import com.example.model.QuestionDifficulty
import com.example.model.QuestionDomain
import com.example.model.QuestionType
import com.example.model.QuizQuestion
import com.example.model.VerseOrLine
import com.example.model.VocabularyItem
import com.example.model.WorkshopQA
import kotlin.random.Random

/**
 * Modular question builders that transform every part of the 10th-grade Persian literature lessons
 * (poetry hemistichs, vocabulary, spelling notes, literary devices, conceptual highlights,
 * workshop Q&As, and wisdom readings) into rich interactive quiz questions.
 */
internal object LessonQuestionExtractors {

    private fun cleanWords(text: String): List<String> =
        text.split(Regex("\\s+")).filter { it.isNotBlank() }

    private fun decideBlankFirst(target: HemistichBlankTarget, rng: Random): Boolean =
        when (target) {
            HemistichBlankTarget.SECOND_ONLY -> false
            HemistichBlankTarget.FIRST_ONLY -> true
            HemistichBlankTarget.MIXED -> rng.nextBoolean()
        }

    fun buildWrittenHemistichQuestion(
        lesson: Lesson,
        line: VerseOrLine,
        blankTarget: HemistichBlankTarget,
        rng: Random,
        index: Int,
        lessonLabelPrefix: String = ""
    ): QuizQuestion {
        val blankFirst = decideBlankFirst(blankTarget, rng)
        val expectedText = if (blankFirst) line.firstHemistich else line.secondHemistich
        val words = cleanWords(expectedText)
        val givenLabel = if (blankFirst) {
            "«... / ${line.secondHemistich}»"
        } else {
            "«${line.firstHemistich} / ...»"
        }
        val missingName = if (blankFirst) "مصرع اول" else "مصرع دوم"
        val titlePrefix = if (lessonLabelPrefix.isNotBlank()) {
            "$lessonLabelPrefix (${lesson.title})"
        } else {
            "${lesson.numberLabel}: ${lesson.title}"
        }

        return QuizQuestion(
            id = "hemistich_written_${lesson.id}_${line.id}_${if (blankFirst) "f" else "s"}_$index",
            type = QuestionType.VERSE_COMPLETION,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = if (line.isMemorizable) QuestionDifficulty.STANDARD else QuestionDifficulty.ADVANCED,
            lessonId = lesson.id,
            lessonTitle = titlePrefix,
            prompt = "بر اساس شعر درس «${lesson.title}»، $missingName این بیت را کامل کنید:\n$givenLabel",
            expectedVerseText = expectedText,
            wordHints = words.shuffled(rng),
            explanation = "بیت کامل: «${line.firstHemistich} / ${line.secondHemistich}»\nمعنی: ${line.meaning}",
            firstHemistich = line.firstHemistich,
            secondHemistich = line.secondHemistich,
            blankFirstHemistich = blankFirst
        )
    }

    fun buildChoiceHemistichQuestion(
        lesson: Lesson,
        line: VerseOrLine,
        blankTarget: HemistichBlankTarget,
        allLessons: List<Lesson>,
        rng: Random,
        index: Int
    ): QuizQuestion? {
        val blankFirst = decideBlankFirst(blankTarget, rng)
        val expectedText = if (blankFirst) line.firstHemistich else line.secondHemistich
        val normExpected = DynamicQuizGenerator.normalizePersianForMatching(expectedText)

        // Prefer hemistichs from other couplets of the same lesson first, then other poetry lessons
        val sameLessonPool = lesson.lines
            .asSequence()
            .filter { it.isPoetry && it.id != line.id && it.secondHemistich.isNotBlank() }
            .map { if (blankFirst) it.firstHemistich else it.secondHemistich }
            .toList()
            .shuffled(rng)

        val otherLessonsPool = allLessons
            .asSequence()
            .filter { it.id != lesson.id }
            .flatMap { it.lines.asSequence() }
            .filter { it.isPoetry && it.secondHemistich.isNotBlank() }
            .map { if (blankFirst) it.firstHemistich else it.secondHemistich }
            .toList()
            .shuffled(rng)

        val distractors = (sameLessonPool + otherLessonsPool)
            .distinctBy { DynamicQuizGenerator.normalizePersianForMatching(it) }
            .filter {
                it.isNotBlank() &&
                    DynamicQuizGenerator.normalizePersianForMatching(it) != normExpected
            }
            .take(3)

        if (distractors.size < 3) return null

        val options = (distractors + expectedText).shuffled(rng)
        val missingName = if (blankFirst) "مصرع اول" else "مصرع دوم"
        val givenDisplay = if (blankFirst) {
            "«... / ${line.secondHemistich}»"
        } else {
            "«${line.firstHemistich} / ...»"
        }

        return QuizQuestion(
            id = "hemistich_choice_${lesson.id}_${line.id}_${if (blankFirst) "f" else "s"}_$index",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = if (line.isMemorizable) QuestionDifficulty.STANDARD else QuestionDifficulty.ADVANCED,
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            prompt = "کدام گزینه $missingName این بیت از درس «${lesson.title}» را به درستی تکمیل می‌کند؟\n$givenDisplay",
            options = options,
            correctOptionIndex = options.indexOf(expectedText),
            explanation = "بیت کامل: «${line.firstHemistich} / ${line.secondHemistich}»\nمعنی: ${line.meaning}",
            firstHemistich = line.firstHemistich,
            secondHemistich = line.secondHemistich,
            blankFirstHemistich = blankFirst
        )
    }

    fun buildVocabularyQuestion(
        lesson: Lesson,
        vocab: VocabularyItem,
        allLessons: List<Lesson>,
        rng: Random,
        index: Int
    ): QuizQuestion? {
        val allVocab = allLessons.flatMap { it.keyVocabulary }.distinctBy { it.meaning }
        val distractors = allVocab
            .filter { it.word != vocab.word && it.meaning != vocab.meaning && it.meaning.isNotBlank() }
            .shuffled(rng)
            .take(3)
            .map { it.meaning }

        if (distractors.size < 3) return null
        val options = (distractors + vocab.meaning).shuffled(rng)
        val spellingInfo = if (vocab.spellingNote.isNotBlank()) " • نکتهٔ املایی: ${vocab.spellingNote}" else ""

        return QuizQuestion(
            id = "dyn_vocab_${lesson.id}_$index",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.STANDARD,
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            prompt = "معنای دقیق واژهٔ «${vocab.word}» در متن درس «${lesson.title}» کدام است؟",
            options = options,
            correctOptionIndex = options.indexOf(vocab.meaning),
            explanation = "واژهٔ «${vocab.word}» به معنای «${vocab.meaning}» است$spellingInfo."
        )
    }

    fun buildSpellingNoteQuestion(
        lesson: Lesson,
        vocab: VocabularyItem,
        allLessons: List<Lesson>,
        rng: Random,
        index: Int
    ): QuizQuestion? {
        if (vocab.spellingNote.isBlank()) return null
        val distractors = allLessons
            .flatMap { it.keyVocabulary }
            .filter { it.word != vocab.word && it.spellingNote.isNotBlank() && it.spellingNote != vocab.spellingNote }
            .distinctBy { it.spellingNote }
            .shuffled(rng)
            .take(3)
            .map { it.word }

        if (distractors.size < 3) return null
        val options = (distractors + vocab.word).shuffled(rng)

        return QuizQuestion(
            id = "dyn_spell_${lesson.id}_$index",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            prompt = "نکتهٔ املایی و معنایی زیر مربوط به کدام واژه از درس «${lesson.title}» است؟\n«${vocab.spellingNote}»",
            options = options,
            correctOptionIndex = options.indexOf(vocab.word),
            explanation = "واژهٔ «${vocab.word}» (به معنای: ${vocab.meaning}) — ${vocab.spellingNote}"
        )
    }

    fun buildLiteraryDeviceQuestion(
        lesson: Lesson,
        line: VerseOrLine,
        allLessons: List<Lesson>,
        rng: Random
    ): QuizQuestion? {
        val targetDevice = line.literaryDevices.firstOrNull() ?: return null
        val otherDevices = allLessons
            .flatMap { it.lines }
            .flatMap { it.literaryDevices }
            .distinct()
            .filter { it != targetDevice && !line.literaryDevices.contains(it) }
            .shuffled(rng)
            .take(3)

        if (otherDevices.size < 3) return null
        val options = (otherDevices + targetDevice).shuffled(rng)
        val snippet = buildString {
            append(line.firstHemistich.take(75))
            if (line.firstHemistich.length > 75) append("...")
            if (line.secondHemistich.isNotBlank()) {
                append(" / ")
                append(line.secondHemistich.take(65))
                if (line.secondHemistich.length > 65) append("...")
            }
        }

        return QuizQuestion(
            id = "dyn_dev_${lesson.id}_${line.id}",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            prompt = "کدام تحلیل یا آرایهٔ ادبی دربارهٔ بخش زیر از درس «${lesson.title}» درست است؟\n«$snippet»",
            options = options,
            correctOptionIndex = options.indexOf(targetDevice),
            explanation = "آرایه‌ها و نکات ادبی این بخش: ${line.literaryDevices.joinToString(" | ")}" +
                if (line.conceptualNote.isNotBlank()) "\nپیام: ${line.conceptualNote}" else ""
        )
    }

    fun buildLineConceptualQuestion(
        lesson: Lesson,
        line: VerseOrLine,
        allLessons: List<Lesson>,
        rng: Random
    ): QuizQuestion? {
        val concept = line.conceptualNote.trim()
        if (concept.length < 18) return null
        val distractorConcepts = allLessons
            .filter { it.id != lesson.id }
            .flatMap { it.lines }
            .map { it.conceptualNote.trim() }
            .filter { it.length >= 18 && it != concept }
            .distinct()
            .shuffled(rng)
            .take(3)

        if (distractorConcepts.size < 3) return null
        val options = (distractorConcepts + concept).shuffled(rng)
        val textPreview = if (line.secondHemistich.isNotBlank()) {
            "«${line.firstHemistich} / ${line.secondHemistich}»"
        } else {
            val truncated = if (line.firstHemistich.length > 90) line.firstHemistich.take(90) + "..." else line.firstHemistich
            "«$truncated»"
        }

        return QuizQuestion(
            id = "dyn_concept_${lesson.id}_${line.id}",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            prompt = "پیام و مفهوم اصلی بخش زیر از درس «${lesson.title}» کدام است؟\n$textPreview",
            options = options,
            correctOptionIndex = options.indexOf(concept),
            explanation = "معنی: ${line.meaning}\nپیام فکری: $concept"
        )
    }

    fun buildWorkshopQuestion(
        lesson: Lesson,
        qa: WorkshopQA,
        allLessons: List<Lesson>,
        rng: Random,
        index: Int
    ): QuizQuestion? {
        val cleanAnswer = qa.answer.trim()
        if (cleanAnswer.isBlank()) return null

        val distractors = allLessons
            .flatMap { it.workshopQAs }
            .filter { it.question != qa.question && it.answer.trim() != cleanAnswer && it.answer.isNotBlank() }
            .distinctBy { it.answer.trim() }
            .shuffled(rng)
            .take(3)
            .map { it.answer.trim() }

        if (distractors.size < 3) return null
        val options = (distractors + cleanAnswer).shuffled(rng)
        val domain = when {
            qa.realm.contains("ادبی") -> QuestionDomain.LITERARY
            qa.realm.contains("فکری") -> QuestionDomain.CONCEPTUAL
            else -> QuestionDomain.LINGUISTIC
        }

        return QuizQuestion(
            id = "dyn_workshop_${lesson.id}_$index",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = domain,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            prompt = "کارگاه درس‌پژوهی (${qa.realm}) — درس «${lesson.title}»:\n${qa.question}",
            options = options,
            correctOptionIndex = options.indexOf(cleanAnswer),
            explanation = "پاسخ کارگاه (${qa.realm}): $cleanAnswer"
        )
    }

    fun buildWisdomReadingQuestion(
        lesson: Lesson,
        allLessons: List<Lesson>,
        rng: Random
    ): QuizQuestion? {
        val wisdom = lesson.wisdomReading ?: return null
        val keyMsg = wisdom.keyMessage.trim()
        if (keyMsg.isBlank()) return null

        val distractors = allLessons
            .mapNotNull { it.wisdomReading }
            .filter { it.title != wisdom.title && it.keyMessage.trim() != keyMsg && it.keyMessage.isNotBlank() }
            .distinctBy { it.keyMessage.trim() }
            .shuffled(rng)
            .take(3)
            .map { it.keyMessage.trim() }

        if (distractors.size < 3) return null
        val options = (distractors + keyMsg).shuffled(rng)

        return QuizQuestion(
            id = "dyn_wisdom_${lesson.id}",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.STANDARD,
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            prompt = "پیام اصلی بخش «${wisdom.typeLabel}: ${wisdom.title}» (از ${wisdom.source}) چیست؟",
            options = options,
            correctOptionIndex = options.indexOf(keyMsg),
            explanation = "پیام کلیدی ${wisdom.title} (${wisdom.source}): $keyMsg"
        )
    }
}
