package com.ykanivets.emojibooks.features.books.models

import kotlinx.serialization.Serializable

@Serializable
data class BookBody(
    val emoji: String,
    val title: String,
    val author: String
) {

    fun toBook(id: String) = Book(
        id = id,
        emoji = emoji,
        title = title,
        author = author
    )
}
