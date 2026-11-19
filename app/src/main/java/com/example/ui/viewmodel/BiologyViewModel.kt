package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.audio.PianoAudioEngine
import com.example.bio3d.BioModelType
import com.example.data.local.QuestionEntity
import com.example.data.local.QuizRecordEntity
import com.example.data.local.UserSettings
import com.example.data.repository.BiologyRepository
import com.example.data.repository.SyncOutcome
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class QuizMode(val titleFa: String, val subtitleFa: String) {
    TIMED_KONKUR("آزمون زمان‌دار کنکور", "۶۰ ثانیه برای هر تست • محاسبه درصد کنکوری و ثبت رکورد"),
    FREE_STUDY("کاوش آزاد و مطالعه تشریحی", "بدون محدودیت زمان • بررسی عمیق مدل ۳بعدی و تحلیل گزینه‌ها"),
    REVIEW_MISTAKES("مرور خطاها و نشان‌شده‌ها", "تمرکز بر تست‌های غلط یا ستاره‌دار شما برای تثبیت کامل")
}

data class QuizSessionState(
    val isActive: Boolean = false,
    val isFinished: Boolean = false,
    val mode: QuizMode = QuizMode.TIMED_KONKUR,
    val lessonFilter: Int = 0,
    val questions: List<QuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val remainingSeconds: Int = 60,
    val totalElapsedSeconds: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val unansweredCount: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val scorePoints: Int = 0,
    val emptyReviewMessage: String? = null
) {
    val currentQuestion: QuestionEntity?
        get() = questions.getOrNull(currentIndex)
}

data class BioLab3DState(
    val activeModel: BioModelType = BioModelType.EUKARYOTIC_CELL,
    val xrayLevel: Float = 0.68f,
    val highlightedPartId: String? = null,
    val autoRotate: Boolean = true,
    val zoomDistance: Float = 3.85f,
    val resetCounter: Int = 0
)

sealed class SyncUiState {
    data object Idle : SyncUiState()
    data object Loading : SyncUiState()
    data class Success(val message: String) : SyncUiState()
    data class Offline(val message: String) : SyncUiState()
    data class Error(val message: String) : SyncUiState()
}

data class LabOverviewStats(
    val totalQuestions: Int = 0,
    val lesson1Count: Int = 0,
    val lesson2Count: Int = 0,
    val answeredCount: Int = 0,
    val masteredCount: Int = 0,
    val mistakeCount: Int = 0,
    val bookmarkCount: Int = 0,
    val bestKonkurPercent: Float = 0f,
    val highestScore: Int = 0
)

class BiologyViewModel(
    private val repository: BiologyRepository,
    val audioEngine: PianoAudioEngine
) : ViewModel() {

    val allQuestions: StateFlow<List<QuestionEntity>> = repository.questions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizRecords: StateFlow<List<QuizRecordEntity>> = repository.quizRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<UserSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    val overviewStats: StateFlow<LabOverviewStats> = combine(allQuestions, quizRecords) { qs, recs ->
        LabOverviewStats(
            totalQuestions = qs.size,
            lesson1Count = qs.count { it.lesson == 1 },
            lesson2Count = qs.count { it.lesson == 2 },
            answeredCount = qs.count { it.timesAnswered > 0 },
            masteredCount = qs.count { it.lastAttemptCorrect == true },
            mistakeCount = qs.count { it.lastAttemptCorrect == false || it.timesWrong > 0 },
            bookmarkCount = qs.count { it.isBookmarked },
            bestKonkurPercent = recs.maxOfOrNull { it.konkurPercent } ?: 0f,
            highestScore = recs.maxOfOrNull { it.scorePoints } ?: 0
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LabOverviewStats())

    private val _quizState = MutableStateFlow(QuizSessionState())
    val quizState: StateFlow<QuizSessionState> = _quizState.asStateFlow()

    private val _lab3DState = MutableStateFlow(BioLab3DState())
    val lab3DState: StateFlow<BioLab3DState> = _lab3DState.asStateFlow()

    private val _syncState = MutableStateFlow<SyncUiState>(SyncUiState.Idle)
    val syncState: StateFlow<SyncUiState> = _syncState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.ensureLocalBankPopulated()
        }
        viewModelScope.launch {
            settings.collect { s ->
                audioEngine.isMusicEnabled = s.pianoEnabled
                audioEngine.isSfxEnabled = s.sfxEnabled
                audioEngine.volume = s.audioVolume
            }
        }
    }

    // --- 3D Bio-Lab Controls ---

    fun selectModel(type: BioModelType, highlightedPart: String? = null) {
        _lab3DState.update {
            it.copy(
                activeModel = type,
                highlightedPartId = highlightedPart,
                xrayLevel = 0.68f,
                zoomDistance = 3.85f,
                resetCounter = it.resetCounter + 1
            )
        }
    }

    fun setXrayLevel(level: Float) {
        _lab3DState.update { it.copy(xrayLevel = level.coerceIn(0f, 1f)) }
    }

    fun toggleHighlightPart(partId: String) {
        _lab3DState.update {
            it.copy(highlightedPartId = if (it.highlightedPartId == partId) null else partId)
        }
    }

    fun toggleAutoRotate() {
        _lab3DState.update { it.copy(autoRotate = !it.autoRotate) }
    }

    fun updateZoom(zoom: Float) {
        _lab3DState.update { it.copy(zoomDistance = zoom.coerceIn(2.2f, 6.2f)) }
    }

    fun reset3DCamera() {
        _lab3DState.update {
            it.copy(
                zoomDistance = 3.85f,
                xrayLevel = 0.68f,
                highlightedPartId = null,
                resetCounter = it.resetCounter + 1
            )
        }
    }

    // --- Quiz Game Engine ---

    fun startQuiz(mode: QuizMode, lessonFilter: Int) {
        timerJob?.cancel()
        val source = allQuestions.value
        val filtered = when (mode) {
            QuizMode.REVIEW_MISTAKES -> source.filter {
                (lessonFilter == 0 || it.lesson == lessonFilter) &&
                    (it.isBookmarked || it.lastAttemptCorrect == false || it.timesWrong > 0)
            }
            else -> source.filter { lessonFilter == 0 || it.lesson == lessonFilter }
        }

        if (filtered.isEmpty()) {
            _quizState.value = QuizSessionState(
                isActive = false,
                isFinished = false,
                mode = mode,
                lessonFilter = lessonFilter,
                emptyReviewMessage = "در حال حاضر سوال غلط یا نشان‌شده‌ای برای این بخش وجود ندارد. ابتدا در آزمون شرکت کنید یا سوالات مهم را ستاره‌دار نمایید."
            )
            return
        }

        val ordered = if (mode == QuizMode.TIMED_KONKUR) {
            filtered.shuffled().take(10)
        } else {
            filtered
        }

        _quizState.value = QuizSessionState(
            isActive = true,
            isFinished = false,
            mode = mode,
            lessonFilter = lessonFilter,
            questions = ordered,
            currentIndex = 0,
            remainingSeconds = 60
        )
        sync3DWithQuestion(ordered.first())
        if (mode == QuizMode.TIMED_KONKUR) {
            startQuestionTimer()
        }
    }

    fun selectOption(index: Int) {
        val current = _quizState.value
        if (!current.isActive || current.isAnswerSubmitted) return
        _quizState.update { it.copy(selectedOptionIndex = index) }
    }

    fun submitAnswer(timedOut: Boolean = false) {
        val state = _quizState.value
        val question = state.currentQuestion ?: return
        if (state.isAnswerSubmitted) return

        timerJob?.cancel()

        val chosen = state.selectedOptionIndex
        val isUnanswered = timedOut && chosen == null
        val isCorrect = !isUnanswered && chosen == question.correctIndex

        if (isCorrect) {
            audioEngine.playCorrectChime()
        } else {
            audioEngine.playWrongChime()
        }

        if (!isUnanswered) {
            viewModelScope.launch {
                repository.recordQuestionAttempt(question.id, isCorrect)
            }
        }

        val newStreak = if (isCorrect) state.currentStreak + 1 else 0
        val bonus = if (isCorrect) 100 + (newStreak - 1) * 25 + (if (state.mode == QuizMode.TIMED_KONKUR) state.remainingSeconds else 0) else 0

        _quizState.update {
            it.copy(
                isAnswerSubmitted = true,
                correctCount = it.correctCount + if (isCorrect) 1 else 0,
                wrongCount = it.wrongCount + if (!isCorrect && !isUnanswered) 1 else 0,
                unansweredCount = it.unansweredCount + if (isUnanswered) 1 else 0,
                currentStreak = newStreak,
                bestStreak = maxOf(it.bestStreak, newStreak),
                scorePoints = it.scorePoints + bonus
            )
        }
    }

    fun nextQuestion() {
        val state = _quizState.value
        val nextIdx = state.currentIndex + 1
        if (nextIdx >= state.questions.size) {
            finishQuiz()
            return
        }
        val nextQ = state.questions[nextIdx]
        _quizState.update {
            it.copy(
                currentIndex = nextIdx,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                remainingSeconds = 60
            )
        }
        sync3DWithQuestion(nextQ)
        if (state.mode == QuizMode.TIMED_KONKUR) {
            startQuestionTimer()
        }
    }

    fun exitQuiz() {
        timerJob?.cancel()
        _quizState.value = QuizSessionState()
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        val state = _quizState.value
        val total = state.questions.size.coerceAtLeast(1)
        val rawPercent = ((state.correctCount * 3f - state.wrongCount) / (total * 3f)) * 100f
        val clampedPercent = rawPercent.coerceIn(-33.3f, 100f)

        viewModelScope.launch {
            repository.saveQuizRecord(
                QuizRecordEntity(
                    timestamp = System.currentTimeMillis(),
                    mode = state.mode.titleFa,
                    lessonFilter = state.lessonFilter,
                    totalQuestions = total,
                    correctCount = state.correctCount,
                    wrongCount = state.wrongCount,
                    unansweredCount = state.unansweredCount,
                    konkurPercent = clampedPercent,
                    scorePoints = state.scorePoints,
                    durationSeconds = state.totalElapsedSeconds
                )
            )
        }

        _quizState.update {
            it.copy(isActive = false, isFinished = true)
        }
    }

    private fun startQuestionTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val current = _quizState.value
                if (!current.isActive || current.isAnswerSubmitted) break
                val nextRem = current.remainingSeconds - 1
                _quizState.update {
                    it.copy(
                        remainingSeconds = nextRem.coerceAtLeast(0),
                        totalElapsedSeconds = it.totalElapsedSeconds + 1
                    )
                }
                if (nextRem <= 0) {
                    submitAnswer(timedOut = true)
                    break
                }
            }
        }
    }

    private fun sync3DWithQuestion(question: QuestionEntity) {
        val model = BioModelType.fromId(question.modelType)
        _lab3DState.update {
            it.copy(
                activeModel = model,
                highlightedPartId = question.highlightedPartId,
                xrayLevel = 0.68f,
                zoomDistance = 3.85f,
                resetCounter = it.resetCounter + 1
            )
        }
    }

    fun toggleBookmark(question: QuestionEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(question.id, question.isBookmarked)
        }
        // Keep active quiz list synced visually too
        _quizState.update { state ->
            state.copy(
                questions = state.questions.map {
                    if (it.id == question.id) it.copy(isBookmarked = !question.isBookmarked) else it
                }
            )
        }
    }

    // --- Audio & Settings ---

    fun togglePianoMusic() {
        val next = !settings.value.pianoEnabled
        viewModelScope.launch { repository.setPianoEnabled(next) }
    }

    fun setPianoEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setPianoEnabled(enabled) }
    }

    fun setSfxEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setSfxEnabled(enabled) }
    }

    fun setAudioVolume(volume: Float) {
        viewModelScope.launch { repository.setAudioVolume(volume) }
    }

    fun updateJsonUrl(url: String) {
        viewModelScope.launch { repository.setUpdateUrl(url) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { repository.setLanguageCode(language.code) }
    }

    fun checkAndFetchUpdate(url: String) {
        viewModelScope.launch {
            _syncState.value = SyncUiState.Loading
            val lang = AppLanguage.fromCode(settings.value.languageCode)
            val strings = AppStrings.forLanguage(lang)
            _syncState.value = when (val outcome = repository.syncFromJsonUrl(url)) {
                SyncOutcome.Offline -> SyncUiState.Offline(strings.syncOfflineMsg)
                is SyncOutcome.Error -> SyncUiState.Error(outcome.message)
                is SyncOutcome.Updated -> SyncUiState.Success(
                    "${strings.syncSuccessPrefix}: ${outcome.title} (${strings.versionPrefix}${outcome.version}, ${outcome.count})"
                )
            }
        }
    }

    fun resetSyncBanner() {
        _syncState.value = SyncUiState.Idle
    }

    fun clearAllUserProgress() {
        viewModelScope.launch {
            repository.clearAllStats()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

class BiologyViewModelFactory(
    private val repository: BiologyRepository,
    private val audioEngine: PianoAudioEngine
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BiologyViewModel::class.java)) {
            return BiologyViewModel(repository, audioEngine) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
