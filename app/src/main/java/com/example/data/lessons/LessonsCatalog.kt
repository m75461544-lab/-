package com.example.data.lessons

import com.example.model.Lesson

object LessonsCatalog {
    val allLessons: List<Lesson> by lazy {
        LessonsPart1.lessons +
            LessonsPart2.lessons +
            LessonsPart3.lessons +
            LessonsPart4.lessons
    }

    fun findById(id: String): Lesson? = allLessons.firstOrNull { it.id == id }
}
