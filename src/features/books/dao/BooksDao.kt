package com.ykanivets.emojibooks.features.books.dao

import org.jetbrains.exposed.sql.Table

object BooksDao : Table("Books") {
    val id = text("id")
    val userId = text("userId")
    val emoji = text("emoji")
    val title = text("title")
    val author = text("author")
}
