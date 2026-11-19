package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val lesson: Int, // 1 = گفتار ۱، 2 = گفتار ۲
    val difficulty: String, // "سخت" | "بسیار سخت" | "دام کنکوری"
    val questionType: String, // "چندموردی" | "شکل‌محور" | "مقایسه‌ای" | "مفهومی"
    val prompt: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int, // 0..3
    val explanation: String,
    val modelType: String, // BioModelType.id
    val highlightedPartId: String? = null,
    val remoteImageUrl: String? = null,
    val figure2dNote: String? = null, // توضیح شکل دوبعدی در صورت وجود نمودار/شکل دوبعدی
    val isBookmarked: Boolean = false,
    val timesAnswered: Int = 0,
    val timesWrong: Int = 0,
    val lastAttemptCorrect: Boolean? = null
)
