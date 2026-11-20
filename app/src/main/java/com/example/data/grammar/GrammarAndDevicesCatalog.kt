package com.example.data.grammar

import com.example.model.GrammarDeviceTopic

object GrammarAndDevicesCatalog {
    val allTopics: List<GrammarDeviceTopic> by lazy {
        GrammarTopicsData.topics + LiteraryDevicesData.topics
    }

    fun findById(id: String): GrammarDeviceTopic? = allTopics.firstOrNull { it.id == id }
}
