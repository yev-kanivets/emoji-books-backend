package com.ykanivets.emojibooks.features.books.interactors

import com.ykanivets.emojibooks.features.books.models.BookBody
import com.ykanivets.emojibooks.features.books.repository.BooksRepository

class UpdateBookInteractor(
    private val booksRepository: BooksRepository = BooksRepository.instance
) {

    data class Request(
        val userId: String,
        val bookId: String,
        val bookBody: BookBody
    )

    sealed class Response {

        object Success : Response()

        object Failure : Response()
    }

    suspend fun execute(request: Request): Response = with(request) {
        val book = bookBody.toBook(id = bookId)
        booksRepository.update(userId, book)
        return Response.Success
    }
}
