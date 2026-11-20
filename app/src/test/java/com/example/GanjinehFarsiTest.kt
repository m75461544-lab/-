package com.example

import com.example.data.grammar.GrammarAndDevicesCatalog
import com.example.data.lessons.LessonsCatalog
import com.example.data.quiz.AdvancedQuestionBank
import com.example.data.quiz.DynamicQuizGenerator
import com.example.model.AppThemeMode
import com.example.model.QuestionType
import com.example.model.StudyProgressState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GanjinehFarsiTest {

    @Test
    fun `default theme mode is dark brown`() {
        val initial = StudyProgressState()
        assertEquals(AppThemeMode.DARK_BROWN, initial.themeMode)
        assertTrue(initial.themeMode.isDark)
    }

    @Test
    fun `all lessons grammar topics and advanced questions have valid integrity`() {
        val lessons = LessonsCatalog.allLessons
        assertTrue("Expected at least 16 lessons", lessons.size >= 16)
        lessons.forEach { lesson ->
            assertTrue(lesson.title.isNotBlank())
            assertTrue(lesson.lines.isNotEmpty())
            assertTrue(lesson.keyVocabulary.isNotEmpty())
            assertTrue(lesson.workshopQAs.isNotEmpty())
        }

        val grammar = GrammarAndDevicesCatalog.allTopics
        assertTrue("Expected at least 12 grammar/device topics", grammar.size >= 12)
        grammar.forEach { topic ->
            assertTrue(topic.title.isNotBlank())
            assertTrue(topic.keyRules.isNotEmpty())
            assertTrue(topic.examTip.isNotBlank())
        }

        val bank = AdvancedQuestionBank.allQuestions
        assertTrue("Expected 60+ advanced questions", bank.size >= 60)
        bank.forEach { q ->
            assertTrue(q.prompt.isNotBlank())
            assertTrue(q.explanation.isNotBlank())
            if (q.type == QuestionType.MULTIPLE_CHOICE) {
                assertEquals(4, q.options.size)
                assertTrue(q.correctOptionIndex in 0..3)
            } else {
                assertTrue(q.expectedVerseText.isNotBlank())
            }
        }
    }

    @Test
    fun `dynamic quiz generator produces half verse completion and normalizes persian input`() {
        val chashmeh = LessonsCatalog.findById("lesson_1")!!
        val quiz = DynamicQuizGenerator.generatePostStudyQuiz(
            lesson = chashmeh,
            allLessons = LessonsCatalog.allLessons,
            questionCount = 7,
            seed = 123
        )
        assertEquals(7, quiz.size)
        assertTrue(quiz.any { it.type == QuestionType.VERSE_COMPLETION })

        val exactEval = DynamicQuizGenerator.evaluateVerseCompletion(
            userInput = "کز همه شیرین سخنی گوش ماند",
            expectedVerse = "کز همه شیرین‌سخنی، گوش ماند"
        )
        assertTrue(exactEval.isCorrect)

        val arabicKeyboardEval = DynamicQuizGenerator.evaluateVerseCompletion(
            userInput = "خداش در همه حال از بلا نگه دارد",
            expectedVerse = "خداش در همه حال از بلا نگه دارد".replace('ی', 'ي').replace('ک', 'ك')
        )
        assertTrue(arabicKeyboardEval.isCorrect)

        val wrongEval = DynamicQuizGenerator.evaluateVerseCompletion(
            userInput = "بیداد ظالمان شما نیز بگذرد",
            expectedVerse = "کز همه شیرین‌سخنی، گوش ماند"
        )
        assertFalse(wrongEval.isCorrect)
    }

    @Test
    fun `common persian spelling traps are clean across all data`() {
        val forbiddenTypos = listOf("مسبه", "افرار", "یعغوب", "تعسیر", "عجائب", "مسولیت", "مسؤلیت")
        val allText = buildString {
            LessonsCatalog.allLessons.forEach { l ->
                append(l.title).append(' ').append(l.summary).append(' ')
                l.lines.forEach {
                    append(it.firstHemistich).append(' ').append(it.secondHemistich).append(' ')
                    append(it.meaning).append(' ').append(it.linguisticNote).append(' ')
                }
            }
            GrammarAndDevicesCatalog.allTopics.forEach { t ->
                append(t.title).append(' ').append(t.summary).append(' ')
                t.keyRules.forEach { append(it).append(' ') }
            }
        }
        forbiddenTypos.forEach { typo ->
            assertFalse("Unexpected typo found: $typo", allText.contains(typo))
        }
    }

    @Test
    fun `quiz generation engine produces valid comprehensive quizzes across lessons`() {
        val lessons = LessonsCatalog.allLessons
        val proseLesson = lessons.first { !it.isPoetry }
        val config = com.example.model.QuizGenerationConfig(
            mode = com.example.model.QuizEngineMode.COMPREHENSIVE_LESSONS,
            scope = com.example.model.LessonPoolScope.SELECTED_LESSONS,
            selectedLessonIds = setOf(proseLesson.id, "lesson_1"),
            questionCount = 12
        )
        val quiz = DynamicQuizGenerator.generateQuiz(config, lessons, seed = 42)
        assertEquals(12, quiz.size)
        quiz.forEach { q ->
            assertTrue(q.prompt.isNotBlank())
            assertTrue(q.explanation.isNotBlank())
            if (q.type == QuestionType.MULTIPLE_CHOICE) {
                assertEquals(4, q.options.size)
                assertTrue(q.correctOptionIndex in 0..3)
                assertEquals(4, q.options.distinct().size)
            } else {
                assertTrue(q.expectedVerseText.isNotBlank())
                assertTrue(q.wordHints.isNotEmpty())
            }
        }
    }

    @Test
    fun `dedicated hemistich completion mode supports written choice and all blank targets`() {
        val lessons = LessonsCatalog.allLessons
        for (target in com.example.model.HemistichBlankTarget.entries) {
            val writtenQuiz = DynamicQuizGenerator.generateQuiz(
                config = com.example.model.QuizGenerationConfig(
                    mode = com.example.model.QuizEngineMode.HEMISTICH_COMPLETION_WRITTEN,
                    scope = com.example.model.LessonPoolScope.MEMORIZATION_ONLY,
                    questionCount = 8,
                    hemistichBlankTarget = target
                ),
                allLessons = lessons,
                seed = 99
            )
            assertEquals(8, writtenQuiz.size)
            writtenQuiz.forEach { q ->
                assertEquals(QuestionType.VERSE_COMPLETION, q.type)
                assertTrue(q.firstHemistich.isNotBlank())
                assertTrue(q.secondHemistich.isNotBlank())
                val expected = if (q.blankFirstHemistich) q.firstHemistich else q.secondHemistich
                assertEquals(expected, q.expectedVerseText)
                val eval = DynamicQuizGenerator.evaluateVerseCompletion(expected, q.expectedVerseText)
                assertTrue(eval.isCorrect)
                assertEquals(eval.totalExpectedWords, eval.matchedWords)
            }

            val choiceQuiz = DynamicQuizGenerator.generateQuiz(
                config = com.example.model.QuizGenerationConfig(
                    mode = com.example.model.QuizEngineMode.HEMISTICH_COMPLETION_CHOICE,
                    scope = com.example.model.LessonPoolScope.MEMORIZATION_ONLY,
                    questionCount = 8,
                    hemistichBlankTarget = target
                ),
                allLessons = lessons,
                seed = 77
            )
            assertEquals(8, choiceQuiz.size)
            choiceQuiz.forEach { q ->
                assertEquals(QuestionType.MULTIPLE_CHOICE, q.type)
                assertEquals(4, q.options.size)
                assertTrue(q.correctOptionIndex in 0..3)
                assertEquals(4, q.options.distinct().size)
                val expected = if (q.blankFirstHemistich) q.firstHemistich else q.secondHemistich
                assertEquals(expected, q.options[q.correctOptionIndex])
            }
        }
    }
}
