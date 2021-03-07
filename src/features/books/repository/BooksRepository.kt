package com.ykanivets.emojibooks.features.books.repository

import com.ykanivets.emojibooks.database.dbQuery
import com.ykanivets.emojibooks.features.books.dao.BooksDao
import com.ykanivets.emojibooks.features.books.models.Book
import org.jetbrains.exposed.sql.*

class BooksRepository {

    suspend fun getAll(userId: String) = dbQuery {
        BooksDao.select { BooksDao.userId eq userId }.map { it.toBook() }
    }

    suspend fun insert(userId: String, book: Book) = dbQuery {
        BooksDao.insert {
            it[id] = book.id
            it[BooksDao.userId] = userId
            it[emoji] = book.emoji
            it[title] = book.title
            it[author] = book.author
        }
    }

    suspend fun update(userId: String, updatedBook: Book) = dbQuery {
        BooksDao.update(
            where = { (BooksDao.userId eq userId) and (BooksDao.id eq updatedBook.id) }
        ) {
            it[emoji] = updatedBook.emoji
            it[title] = updatedBook.title
            it[author] = updatedBook.author
        }
    }

    suspend fun delete(userId: String, id: String) = dbQuery {
        BooksDao.deleteWhere { (BooksDao.userId eq userId) and (BooksDao.id eq id) }
    }

    private fun ResultRow.toBook() = Book(
        id = this[BooksDao.id],
        emoji = this[BooksDao.emoji],
        title = this[BooksDao.title],
        author = this[BooksDao.author],
    )

    companion object {

        val instance = BooksRepository()
    }
}