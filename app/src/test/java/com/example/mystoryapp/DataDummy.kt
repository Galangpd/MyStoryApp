package com.example.mystoryapp

import com.example.mystoryapp.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "", "", "", "", "", 0.0, 0.0
            )
            items.add(story)
        }
        return items
    }
}