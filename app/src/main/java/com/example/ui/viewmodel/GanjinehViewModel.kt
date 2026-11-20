package com.example.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PreferencesRepository
import com.example.data.grammar.GrammarAndDevicesCatalog
import com.example.data.lessons.LessonsCatalog
import com.example.data.quiz.AdvancedQuestionBank
import com.example.data.quiz.DynamicQuizGenerator
import com.example.model.AppThemeMode
import com.example.model.GrammarCategory
import com.example.model.GrammarDeviceTopic
import com.example.model.HemistichBlankTarget
import com.example.model.Lesson
import com.example.model.LessonCategory
import com.example.model.LessonPoolScope
import com.example.model.QuestionDifficulty
import com.example.model.QuestionDomain
import com.example.model.QuestionType
import com.example.model.QuizEngineMode
import com.example.model.QuizGenerationConfig
import com.example.model.QuizQuestion
import com.example.model.QuizSessionState
import com.example.model.StudyProgressState
import com.example.model.StudySessionReport
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val THIRTY_MINUTES_SECONDS = 30 * 60 // 1800 seconds

@Immutable
data class TimerSessionState(
    val activeLessonId: String? = null,
    val activeLessonTitle: String = "",
    val elapsedSeconds: Int = 0,
    val targetSeconds: Int = THIRTY_MINUTES_SECONDS,
    val isRunning: Boolean = false
) {
    val progressFraction: Float
        get() = if (targetSeconds <= 0) 0f else (elapsedSeconds.toFloat() / targetSeconds).coerceIn(0f, 1f)
}

class GanjinehViewModel(
    private val prefsRepo: PreferencesRepository
) : ViewModel() {

    val allLessons: List<Lesson> = LessonsCatalog.allLessons
    val allGrammarTopics: List<GrammarDeviceTopic> = GrammarAndDevicesCatalog.allTopics
    val advancedQuestions: List<QuizQuestion> = AdvancedQuestionBank.allQuestions

    val progressState: StateFlow<StudyProgressState> = prefsRepo.progressFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StudyProgressState()
    )

    private val _timerState = MutableStateFlow(TimerSessionState())
    val timerState: StateFlow<TimerSessionState> = _timerState.asStateFlow()

    private val _studyReport = MutableStateFlow<StudySessionReport?>(null)
    val studyReport: StateFlow<StudySessionReport?> = _studyReport.asStateFlow()

    private val _quizState = MutableStateFlow<QuizSessionState?>(null)
    val quizState: StateFlow<QuizSessionState?> = _quizState.asStateFlow()

    private val _lessonSearchQuery = MutableStateFlow("")
    val lessonSearchQuery: StateFlow<String> = _lessonSearchQuery.asStateFlow()

    private val _lessonCategory = MutableStateFlow(LessonCategory.ALL)
    val lessonCategory: StateFlow<LessonCategory> = _lessonCategory.asStateFlow()

    private val _grammarCategory = MutableStateFlow(GrammarCategory.ALL)
    val grammarCategory: StateFlow<GrammarCategory> = _grammarCategory.asStateFlow()

    private val _bankDomainFilter = MutableStateFlow<QuestionDomain?>(null)
    val bankDomainFilter: StateFlow<QuestionDomain?> = _bankDomainFilter.asStateFlow()

    private val _bankDifficultyFilter = MutableStateFlow<QuestionDifficulty?>(null)
    val bankDifficultyFilter: StateFlow<QuestionDifficulty?> = _bankDifficultyFilter.asStateFlow()

    private val _showThemeSheet = MutableStateFlow(false)
    val showThemeSheet: StateFlow<Boolean> = _showThemeSheet.asStateFlow()

    private val _quizConfig = MutableStateFlow(QuizGenerationConfig())
    val quizConfig: StateFlow<QuizGenerationConfig> = _quizConfig.asStateFlow()

    private var timerJob: Job? = null

    fun setThemeSheetVisible(visible: Boolean) {
        _showThemeSheet.value = visible
    }

    fun selectTheme(mode: AppThemeMode) {
        viewModelScope.launch {
            prefsRepo.setThemeMode(mode)
        }
    }

    fun updateLessonSearch(query: String) {
        _lessonSearchQuery.value = query
    }

    fun selectLessonCategory(category: LessonCategory) {
        _lessonCategory.value = category
    }

    fun selectGrammarCategory(category: GrammarCategory) {
        _grammarCategory.value = category
    }

    fun selectBankDomain(domain: QuestionDomain?) {
        _bankDomainFilter.value = domain
    }

    fun selectBankDifficulty(difficulty: QuestionDifficulty?) {
        _bankDifficultyFilter.value = difficulty
    }

    fun toggleLineRead(lineId: String) {
        viewModelScope.launch {
            prefsRepo.toggleLineRead(lineId)
        }
    }

    fun startOrResumeLessonTimer(lesson: Lesson) {
        val current = _timerState.value
        if (current.activeLessonId == lesson.id && current.isRunning) return

        // If switching from another lesson with accumulated unsaved seconds, save them cleanly
        if (current.activeLessonId != null && current.activeLessonId != lesson.id && current.elapsedSeconds > 0) {
            val prevId = current.activeLessonId
            val prevSecs = current.elapsedSeconds
            viewModelScope.launch {
                prefsRepo.recordStudySeconds(prevId, prevSecs)
            }
        }

        val initialElapsed = if (current.activeLessonId == lesson.id) current.elapsedSeconds else 0
        _timerState.value = TimerSessionState(
            activeLessonId = lesson.id,
            activeLessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            elapsedSeconds = initialElapsed,
            targetSeconds = THIRTY_MINUTES_SECONDS,
            isRunning = true
        )
        launchTicker()
    }

    fun pauseOrResumeTimer() {
        val current = _timerState.value
        if (current.activeLessonId == null) return
        if (current.isRunning) {
            timerJob?.cancel()
            _timerState.update { it.copy(isRunning = false) }
        } else {
            _timerState.update { it.copy(isRunning = true) }
            launchTicker()
        }
    }

    private fun launchTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val state = _timerState.value
                if (!state.isRunning || state.activeLessonId == null) break
                val nextSeconds = state.elapsedSeconds + 1
                _timerState.update { it.copy(elapsedSeconds = nextSeconds) }
                if (nextSeconds >= state.targetSeconds) {
                    // Automatically trigger the 30-minute study report and post-study quiz!
                    finishStudySession(reachedGoalAutomatically = true)
                    break
                }
            }
        }
    }

    /**
     * Completes the current study session (either automatically at 30:00 or when the student finishes studying),
     * persists study duration & marks studied lines, builds a detailed StudySessionReport, and prepares
     * custom post-study questions (including half-verse completion).
     */
    fun finishStudySession(
        lessonOverride: Lesson? = null,
        reachedGoalAutomatically: Boolean = false
    ) {
        timerJob?.cancel()
        val currentTimer = _timerState.value
        val lesson = lessonOverride
            ?: currentTimer.activeLessonId?.let { LessonsCatalog.findById(it) }
            ?: return

        val sessionSecs = if (currentTimer.activeLessonId == lesson.id) {
            currentTimer.elapsedSeconds
        } else 0

        val reachedGoal = reachedGoalAutomatically || sessionSecs >= THIRTY_MINUTES_SECONDS

        // Mark lines seen during this session proportional to time or at least current read lines
        val existingRead = progressState.value.readLineIds
        val lessonLineIds = lesson.lines.map { it.id }
        val alreadyReadCount = lessonLineIds.count { it in existingRead }
        val autoCoveredCount = when {
            reachedGoal -> lessonLineIds.size
            sessionSecs >= 600 -> ((lessonLineIds.size * 2) / 3).coerceAtLeast(1)
            sessionSecs >= 60 -> (lessonLineIds.size / 2).coerceAtLeast(1)
            else -> alreadyReadCount.coerceAtLeast(1).coerceAtMost(lessonLineIds.size)
        }
        val linesToMark = lessonLineIds.take(autoCoveredCount)

        val previousSavedSeconds = progressState.value.lessonStudySeconds[lesson.id] ?: 0
        val newTotalSeconds = previousSavedSeconds + sessionSecs

        viewModelScope.launch {
            prefsRepo.markLinesRead(linesToMark)
            prefsRepo.recordStudySeconds(
                lessonId = lesson.id,
                addedSeconds = sessionSecs,
                markLessonCompleted = reachedGoal || linesToMark.size == lessonLineIds.size
            )
        }

        val generatedQuestions = DynamicQuizGenerator.generatePostStudyQuiz(
            lesson = lesson,
            allLessons = allLessons,
            questionCount = 7
        )

        _timerState.value = TimerSessionState()
        _studyReport.value = StudySessionReport(
            lessonId = lesson.id,
            lessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            sessionSeconds = sessionSecs,
            totalLessonSeconds = newTotalSeconds,
            targetSeconds = THIRTY_MINUTES_SECONDS,
            sectionsCoveredCount = maxOf(alreadyReadCount, linesToMark.size),
            totalSectionsCount = lessonLineIds.size.coerceAtLeast(1),
            reachedThirtyMinGoal = reachedGoal,
            generatedQuestions = generatedQuestions
        )
    }

    /**
     * Quick simulation helper if user wants to preview the full 30-minute end-of-session report + auto-quiz immediately.
     */
    fun simulateThirtyMinCompletion(lesson: Lesson) {
        _timerState.value = TimerSessionState(
            activeLessonId = lesson.id,
            activeLessonTitle = "${lesson.numberLabel}: ${lesson.title}",
            elapsedSeconds = THIRTY_MINUTES_SECONDS,
            targetSeconds = THIRTY_MINUTES_SECONDS,
            isRunning = false
        )
        finishStudySession(lessonOverride = lesson, reachedGoalAutomatically = true)
    }

    fun dismissStudyReport() {
        _studyReport.value = null
    }

    fun startQuizFromReport(report: StudySessionReport) {
        _studyReport.value = null
        _quizState.value = QuizSessionState(
            title = "آزمون هوشمند پس از مطالعه",
            subtitle = "طراحی‌شده از ${report.lessonTitle} (همراه با تکمیل مصرع شعر)",
            questions = report.generatedQuestions
        )
    }

    fun updateQuizEngineMode(mode: QuizEngineMode) {
        _quizConfig.update { current ->
            val adjustedScope = if (mode != QuizEngineMode.COMPREHENSIVE_LESSONS &&
                current.scope == LessonPoolScope.ALL_LESSONS
            ) {
                LessonPoolScope.MEMORIZATION_ONLY
            } else current.scope
            current.copy(mode = mode, scope = adjustedScope)
        }
    }

    fun toggleQuizSelectedLesson(lessonId: String) {
        _quizConfig.update { current ->
            val updated = if (lessonId in current.selectedLessonIds) {
                current.selectedLessonIds - lessonId
            } else current.selectedLessonIds + lessonId
            current.copy(scope = LessonPoolScope.SELECTED_LESSONS, selectedLessonIds = updated)
        }
    }

    fun updateQuizConfig(transform: (QuizGenerationConfig) -> QuizGenerationConfig) =
        _quizConfig.update(transform)

    fun launchConfiguredQuiz() {
        val cfg = _quizConfig.value
        val qs = DynamicQuizGenerator.generateQuiz(config = cfg, allLessons = allLessons)
        val scopeLabel = when (cfg.scope) {
            LessonPoolScope.ALL_LESSONS -> "همهٔ درس‌های کتاب"
            LessonPoolScope.MEMORIZATION_ONLY -> "شعرهای حفظی کتاب"
            LessonPoolScope.SELECTED_LESSONS -> if (cfg.selectedLessonIds.isEmpty()) {
                "همهٔ درس‌ها"
            } else {
                "${cfg.selectedLessonIds.size} درس انتخابی"
            }
        }
        val subtitle = when (cfg.mode) {
            QuizEngineMode.COMPREHENSIVE_LESSONS -> "$scopeLabel • طراحی پویا از محتوای دروس"
            QuizEngineMode.HEMISTICH_COMPLETION_WRITTEN -> "$scopeLabel • تکمیل ${cfg.hemistichBlankTarget.titleFa}"
            QuizEngineMode.HEMISTICH_COMPLETION_CHOICE -> "$scopeLabel • انتخاب ${cfg.hemistichBlankTarget.titleFa}"
        }
        _quizState.value = QuizSessionState(
            title = cfg.mode.shortTitleFa,
            subtitle = subtitle,
            questions = qs
        )
    }

    fun startMemorizationDrill(
        lessonId: String? = null,
        blankTarget: HemistichBlankTarget = HemistichBlankTarget.SECOND_ONLY,
        writtenMode: Boolean = true
    ) {
        val qs = DynamicQuizGenerator.generateMemorizationCompletionDrill(
            lessons = allLessons,
            targetLessonId = lessonId,
            count = 8,
            blankTarget = blankTarget,
            writtenMode = writtenMode
        )
        val lessonLabel = lessonId?.let { LessonsCatalog.findById(it)?.title } ?: "همهٔ شعرهای حفظی"
        _quizState.value = QuizSessionState(
            title = if (writtenMode) "تکمیل مصرع شعر (نوشتاری)" else "تکمیل مصرع شعر (چهارگزینه‌ای)",
            subtitle = "$lessonLabel • ${blankTarget.titleFa}",
            questions = qs
        )
    }

    fun startAdvancedBankQuiz(
        domain: QuestionDomain? = null,
        difficulty: QuestionDifficulty? = null,
        count: Int = 10
    ) {
        val filtered = advancedQuestions.filter { q ->
            (domain == null || q.domain == domain) &&
                (difficulty == null || q.difficulty == difficulty)
        }.ifEmpty { advancedQuestions }.shuffled().take(count)

        _quizState.value = QuizSessionState(
            title = "آزمون جامع سطح پیشرفته",
            subtitle = buildString {
                append(domain?.titleFa ?: "همهٔ قلمروها")
                append(" • ")
                append(difficulty?.titleFa ?: "سطح ترکیبی")
            },
            questions = filtered
        )
    }

    fun startCustomLessonQuiz(lesson: Lesson) {
        val qs = DynamicQuizGenerator.generatePostStudyQuiz(
            lesson = lesson,
            allLessons = allLessons,
            questionCount = 8
        )
        _quizState.value = QuizSessionState(
            title = "آزمون اختصاصی ${lesson.numberLabel}",
            subtitle = lesson.title,
            questions = qs
        )
    }

    fun selectQuizOption(optionIndex: Int) {
        val current = _quizState.value ?: return
        if (current.hasSubmittedCurrent) return
        _quizState.value = current.copy(selectedOptionIndex = optionIndex)
    }

    fun updateVerseInput(input: String) {
        val current = _quizState.value ?: return
        if (current.hasSubmittedCurrent) return
        _quizState.value = current.copy(verseUserInput = input)
    }

    fun appendHintWordToInput(word: String) {
        val current = _quizState.value ?: return
        if (current.hasSubmittedCurrent) return
        val trimmed = current.verseUserInput.trimEnd()
        val updated = if (trimmed.isEmpty()) word else "$trimmed $word"
        _quizState.value = current.copy(verseUserInput = updated)
    }

    fun removeLastVerseWord() {
        val current = _quizState.value ?: return
        if (current.hasSubmittedCurrent) return
        val words = current.verseUserInput.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        val updated = if (words.size <= 1) "" else words.dropLast(1).joinToString(" ")
        _quizState.value = current.copy(verseUserInput = updated)
    }

    fun revealNextHintWord() {
        val current = _quizState.value ?: return
        val q = current.currentQuestion ?: return
        val expectedWords = q.expectedVerseText.split(Regex("\\s+")).filter { it.isNotBlank() }
        val nextCount = (current.revealedHintWordCount + 1).coerceAtMost(expectedWords.size)
        val prefix = expectedWords.take(nextCount).joinToString(" ")
        _quizState.value = current.copy(
            revealedHintWordCount = nextCount,
            verseUserInput = prefix
        )
    }

    fun submitCurrentQuizAnswer() {
        val current = _quizState.value ?: return
        val q = current.currentQuestion ?: return
        if (current.hasSubmittedCurrent) return

        when (q.type) {
            QuestionType.MULTIPLE_CHOICE -> {
                val chosen = current.selectedOptionIndex ?: return
                val isCorrect = chosen == q.correctOptionIndex
                _quizState.value = current.copy(
                    hasSubmittedCurrent = true,
                    currentIsCorrect = isCorrect,
                    currentFeedback = if (isCorrect) "پاسخ کاملاً درست است!" else "پاسخ صحیح گزینهٔ دیگر است؛ تحلیل تشریحی را بخوانید.",
                    correctCount = current.correctCount + if (isCorrect) 1 else 0
                )
            }

            QuestionType.VERSE_COMPLETION -> {
                val eval = DynamicQuizGenerator.evaluateVerseCompletion(
                    userInput = current.verseUserInput,
                    expectedVerse = q.expectedVerseText
                )
                _quizState.value = current.copy(
                    hasSubmittedCurrent = true,
                    currentIsCorrect = eval.isCorrect,
                    currentFeedback = eval.feedback,
                    lastVerseEval = eval,
                    correctCount = current.correctCount + if (eval.isCorrect) 1 else 0
                )
            }
        }
    }

    fun nextQuizQuestion() {
        val current = _quizState.value ?: return
        val nextIdx = current.currentIndex + 1
        if (nextIdx >= current.questions.size) {
            _quizState.value = current.copy(isFinished = true)
            viewModelScope.launch {
                prefsRepo.recordQuizResult(
                    correct = current.correctCount,
                    total = current.questions.size
                )
            }
        } else {
            _quizState.value = current.copy(
                currentIndex = nextIdx,
                selectedOptionIndex = null,
                verseUserInput = "",
                hasSubmittedCurrent = false,
                currentIsCorrect = false,
                currentFeedback = "",
                lastVerseEval = null,
                revealedHintWordCount = 0
            )
        }
    }

    fun closeQuizSession() {
        _quizState.value = null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

class GanjinehViewModelFactory(
    private val repository: PreferencesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GanjinehViewModel::class.java)) {
            return GanjinehViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
