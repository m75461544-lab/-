package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppThemeMode
import com.example.model.StudyProgressState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.ganjinehDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "ganjineh_farsi_prefs"
)

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val COMPLETED_LESSONS = stringSetPreferencesKey("completed_lessons")
        val READ_LINE_IDS = stringSetPreferencesKey("read_line_ids")
        val LESSON_SECONDS_MAP = stringPreferencesKey("lesson_seconds_map")
        val TOTAL_QUIZZES = intPreferencesKey("total_quizzes")
        val TOTAL_QUESTIONS_ANSWERED = intPreferencesKey("total_questions_answered")
        val TOTAL_CORRECT = intPreferencesKey("total_correct")
        val BEST_SCORE_PERCENT = intPreferencesKey("best_score_percent")
        val TOTAL_STUDY_SESSIONS = intPreferencesKey("total_study_sessions")
    }

    val progressFlow: Flow<StudyProgressState> = context.ganjinehDataStore.data.map { prefs ->
        val themeName = prefs[Keys.THEME_MODE] ?: AppThemeMode.DARK_BROWN.name
        val themeMode = runCatching { AppThemeMode.valueOf(themeName) }
            .getOrDefault(AppThemeMode.DARK_BROWN)

        val secondsMap = decodeSecondsMap(prefs[Keys.LESSON_SECONDS_MAP].orEmpty())

        StudyProgressState(
            themeMode = themeMode,
            lessonStudySeconds = secondsMap,
            completedLessonIds = prefs[Keys.COMPLETED_LESSONS] ?: emptySet(),
            readLineIds = prefs[Keys.READ_LINE_IDS] ?: emptySet(),
            totalQuizzesCompleted = prefs[Keys.TOTAL_QUIZZES] ?: 0,
            totalQuestionsAnswered = prefs[Keys.TOTAL_QUESTIONS_ANSWERED] ?: 0,
            totalCorrectAnswers = prefs[Keys.TOTAL_CORRECT] ?: 0,
            bestScorePercent = prefs[Keys.BEST_SCORE_PERCENT] ?: 0,
            totalStudySessions = prefs[Keys.TOTAL_STUDY_SESSIONS] ?: 0
        )
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.ganjinehDataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = mode.name
        }
    }

    suspend fun toggleLineRead(lineId: String) {
        context.ganjinehDataStore.edit { prefs ->
            val current = prefs[Keys.READ_LINE_IDS] ?: emptySet()
            prefs[Keys.READ_LINE_IDS] = if (lineId in current) current - lineId else current + lineId
        }
    }

    suspend fun markLinesRead(lineIds: Collection<String>) {
        if (lineIds.isEmpty()) return
        context.ganjinehDataStore.edit { prefs ->
            val current = prefs[Keys.READ_LINE_IDS] ?: emptySet()
            prefs[Keys.READ_LINE_IDS] = current + lineIds
        }
    }

    suspend fun recordStudySeconds(
        lessonId: String,
        addedSeconds: Int,
        markLessonCompleted: Boolean = false
    ) {
        context.ganjinehDataStore.edit { prefs ->
            if (addedSeconds > 0) {
                val map = decodeSecondsMap(prefs[Keys.LESSON_SECONDS_MAP].orEmpty()).toMutableMap()
                map[lessonId] = (map[lessonId] ?: 0) + addedSeconds
                prefs[Keys.LESSON_SECONDS_MAP] = encodeSecondsMap(map)
            }
            prefs[Keys.TOTAL_STUDY_SESSIONS] = (prefs[Keys.TOTAL_STUDY_SESSIONS] ?: 0) + 1
            if (markLessonCompleted) {
                val completed = prefs[Keys.COMPLETED_LESSONS] ?: emptySet()
                prefs[Keys.COMPLETED_LESSONS] = completed + lessonId
            }
        }
    }

    suspend fun recordQuizResult(correct: Int, total: Int) {
        if (total <= 0) return
        val percent = ((correct * 100f) / total).toInt().coerceIn(0, 100)
        context.ganjinehDataStore.edit { prefs ->
            prefs[Keys.TOTAL_QUIZZES] = (prefs[Keys.TOTAL_QUIZZES] ?: 0) + 1
            prefs[Keys.TOTAL_QUESTIONS_ANSWERED] = (prefs[Keys.TOTAL_QUESTIONS_ANSWERED] ?: 0) + total
            prefs[Keys.TOTAL_CORRECT] = (prefs[Keys.TOTAL_CORRECT] ?: 0) + correct
            val prevBest = prefs[Keys.BEST_SCORE_PERCENT] ?: 0
            if (percent > prevBest) {
                prefs[Keys.BEST_SCORE_PERCENT] = percent
            }
        }
    }

    private fun encodeSecondsMap(map: Map<String, Int>): String =
        map.entries.joinToString(";") { "${it.key}=${it.value}" }

    private fun decodeSecondsMap(raw: String): Map<String, Int> {
        if (raw.isBlank()) return emptyMap()
        val result = mutableMapOf<String, Int>()
        raw.split(";").forEach { entry ->
            val parts = entry.split("=")
            if (parts.size == 2) {
                val secs = parts[1].toIntOrNull()
                if (parts[0].isNotBlank() && secs != null) {
                    result[parts[0]] = secs
                }
            }
        }
        return result
    }
}
