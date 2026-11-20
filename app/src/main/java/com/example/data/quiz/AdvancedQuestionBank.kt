package com.example.data.quiz

import com.example.model.QuizQuestion

object AdvancedQuestionBank {
    val allQuestions: List<QuizQuestion> by lazy {
        AdvancedBankPart1.questions +
            AdvancedBankPart2.questions +
            AdvancedBankPart3.questions
    }

    fun forLesson(lessonId: String): List<QuizQuestion> =
        allQuestions.filter { it.lessonId == lessonId }
}
