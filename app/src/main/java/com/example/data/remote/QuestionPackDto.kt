package com.example.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionPackDto(
    val version: Int = 1,
    val packTitle: String = "",
    val questions: List<QuestionRemoteDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class QuestionRemoteDto(
    val id: String,
    val lesson: Int,
    val difficulty: String = "سخت",
    val questionType: String = "مفهومی",
    val prompt: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int,
    val explanation: String,
    val modelType: String = "EUKARYOTIC_CELL",
    val highlightedPartId: String? = null,
    val remoteImageUrl: String? = null,
    val figure2dNote: String? = null
)
