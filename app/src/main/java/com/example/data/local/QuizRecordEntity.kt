package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_records")
data class QuizRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val mode: String, // "آزمون زمان‌دار" | "کاوش آزاد" | "مرور خطاها"
    val lessonFilter: Int, // 0 = کل فصل ۱ (گفتار ۱ و ۲)، 1 = گفتار ۱، 2 = گفتار ۲
    val totalQuestions: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val unansweredCount: Int,
    val konkurPercent: Float,
    val scorePoints: Int,
    val durationSeconds: Int
)
