package com.example.ui.i18n

import com.example.data.local.QuestionEntity

data class QuestionTranslation(
    val prompt: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val explanation: String,
    val figure2dNote: String? = null
)

fun QuestionEntity.localized(language: AppLanguage): QuestionEntity {
    if (language == AppLanguage.FA) return this
    val map = when (language) {
        AppLanguage.AR -> QuestionTranslationsAr.map
        AppLanguage.EN -> QuestionTranslationsEn.map
        AppLanguage.FA -> emptyMap()
    }
    val tr = map[id] ?: return this
    return copy(
        prompt = tr.prompt,
        optionA = tr.optionA,
        optionB = tr.optionB,
        optionC = tr.optionC,
        optionD = tr.optionD,
        explanation = tr.explanation,
        figure2dNote = tr.figure2dNote ?: figure2dNote
    )
}
