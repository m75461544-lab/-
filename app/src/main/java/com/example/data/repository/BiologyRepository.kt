package com.example.data.repository

import com.example.bio3d.BioModelType
import com.example.data.local.BiologyDao
import com.example.data.local.InitialQuestionBank
import com.example.data.local.QuestionEntity
import com.example.data.local.QuizRecordEntity
import com.example.data.local.SettingsDataStore
import com.example.data.local.UserSettings
import com.example.data.remote.QuestionUpdateService
import com.example.data.remote.UpdateFetchResult
import kotlinx.coroutines.flow.Flow

sealed class SyncOutcome {
    data object Offline : SyncOutcome()
    data class Error(val message: String) : SyncOutcome()
    data class Updated(val count: Int, val version: Int, val title: String) : SyncOutcome()
}

class BiologyRepository(
    private val dao: BiologyDao,
    private val settingsStore: SettingsDataStore,
    private val updateService: QuestionUpdateService
) {
    val questions: Flow<List<QuestionEntity>> = dao.observeAllQuestions()
    val quizRecords: Flow<List<QuizRecordEntity>> = dao.observeQuizRecords()
    val settings: Flow<UserSettings> = settingsStore.settingsFlow

    suspend fun ensureLocalBankPopulated() {
        if (dao.getQuestionCount() == 0) {
            dao.upsertQuestions(InitialQuestionBank.questions)
        }
    }

    suspend fun toggleBookmark(id: String, currentValue: Boolean) {
        dao.setBookmark(id, !currentValue)
    }

    suspend fun recordQuestionAttempt(id: String, isCorrect: Boolean) {
        dao.recordQuestionAttempt(id, isCorrect)
    }

    suspend fun saveQuizRecord(record: QuizRecordEntity) {
        dao.insertQuizRecord(record)
    }

    suspend fun clearAllStats() {
        dao.clearQuizRecords()
        dao.resetQuestionStats()
    }

    suspend fun setPianoEnabled(enabled: Boolean) = settingsStore.setPianoEnabled(enabled)
    suspend fun setSfxEnabled(enabled: Boolean) = settingsStore.setSfxEnabled(enabled)
    suspend fun setAudioVolume(volume: Float) = settingsStore.setAudioVolume(volume)
    suspend fun setUpdateUrl(url: String) = settingsStore.setUpdateJsonUrl(url)

    suspend fun syncFromJsonUrl(url: String): SyncOutcome {
        settingsStore.setUpdateJsonUrl(url)
        return when (val res = updateService.fetchQuestionPack(url)) {
            UpdateFetchResult.NoInternet -> SyncOutcome.Offline
            is UpdateFetchResult.InvalidUrl -> SyncOutcome.Error(res.message)
            is UpdateFetchResult.Failure -> SyncOutcome.Error(res.reason)
            is UpdateFetchResult.Success -> {
                val existingMap = dao.getAllQuestionsOnce().associateBy { it.id }
                val merged = res.pack.questions.map { dto ->
                    val old = existingMap[dto.id]
                    QuestionEntity(
                        id = dto.id,
                        lesson = dto.lesson,
                        difficulty = dto.difficulty,
                        questionType = dto.questionType,
                        prompt = dto.prompt,
                        optionA = dto.optionA,
                        optionB = dto.optionB,
                        optionC = dto.optionC,
                        optionD = dto.optionD,
                        correctIndex = dto.correctIndex,
                        explanation = dto.explanation,
                        modelType = BioModelType.fromId(dto.modelType).id,
                        highlightedPartId = dto.highlightedPartId,
                        remoteImageUrl = dto.remoteImageUrl,
                        figure2dNote = dto.figure2dNote,
                        isBookmarked = old?.isBookmarked ?: false,
                        timesAnswered = old?.timesAnswered ?: 0,
                        timesWrong = old?.timesWrong ?: 0,
                        lastAttemptCorrect = old?.lastAttemptCorrect
                    )
                }
                dao.upsertQuestions(merged)
                settingsStore.recordPackUpdate(res.pack.version, System.currentTimeMillis())
                SyncOutcome.Updated(
                    count = merged.size,
                    version = res.pack.version,
                    title = res.pack.packTitle.ifBlank { "بسته تکمیلی زیست دهم" }
                )
            }
        }
    }
}
