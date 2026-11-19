package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BiologyDao {

    @Query("SELECT * FROM questions ORDER BY lesson ASC, id ASC")
    fun observeAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestionsOnce(): List<QuestionEntity>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuestions(questions: List<QuestionEntity>)

    @Query("UPDATE questions SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setBookmark(id: String, bookmarked: Boolean)

    @Query(
        "UPDATE questions SET timesAnswered = timesAnswered + 1, " +
            "timesWrong = timesWrong + CASE WHEN :isCorrect THEN 0 ELSE 1 END, " +
            "lastAttemptCorrect = :isCorrect WHERE id = :id"
    )
    suspend fun recordQuestionAttempt(id: String, isCorrect: Boolean)

    @Query("SELECT * FROM quiz_records ORDER BY timestamp DESC")
    fun observeQuizRecords(): Flow<List<QuizRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizRecord(record: QuizRecordEntity)

    @Query("DELETE FROM quiz_records")
    suspend fun clearQuizRecords()

    @Query("UPDATE questions SET timesAnswered = 0, timesWrong = 0, lastAttemptCorrect = NULL")
    suspend fun resetQuestionStats()
}
