package com.ykanivets.emojibooks.features.books.repository

import com.ykanivets.emojibooks.features.books.models.Book

class BooksRepository {

    private val books = mutableListOf<Book>()

    fun getAll() = books

    fun insert(book: Book) = books.add(book)

    fun update(updatedBook: Book) = books.replaceAll { book -> if (book.id == updatedBook.id) updatedBook else book }

    fun delete(id: String) = books.removeIf { book -> book.id == id }
}