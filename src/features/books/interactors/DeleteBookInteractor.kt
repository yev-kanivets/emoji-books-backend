package com.ykanivets.emojibooks.features.books.interactors

import com.ykanivets.emojibooks.features.books.repository.BooksRepository

class DeleteBookInteractor(
    private val booksRepository: BooksRepository = BooksRepository.instance
) {

    data class Request(
        val userId: String,
        val bookId: String
    )

    sealed class Response {

        object Success : Response()

        object Failure : Response()
    }

    suspend fun execute(request: Request): Response = with(request) {
        booksRepository.delete(userId, bookId)
        return Response.Success
    }
}
