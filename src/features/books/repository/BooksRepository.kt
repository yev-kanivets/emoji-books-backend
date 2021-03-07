package com.ykanivets.emojibooks.features.books.repository

import com.ykanivets.emojibooks.features.books.models.Book

class BooksRepository {

    private val books = mutableMapOf<String, MutableList<Book>>()

    fun getAll(userId: String) = getUserBooks(userId).toList()

    fun insert(userId: String, book: Book) = getUserBooks(userId).add(book)

    fun update(userId: String, updatedBook: Book) = getUserBooks(userId).replaceAll { book ->
        if (book.id == updatedBook.id) updatedBook else book
    }

    fun delete(userId: String, id: String) = getUserBooks(userId).removeIf { book -> book.id == id }

    private fun getUserBooks(userId: String) = books.getOrPut(userId) { mutableListOf() }

    companion object {

        val instance = BooksRepository()
    }
}