package com.ykanivets.emojibooks.features.books.interactors

import com.ykanivets.emojibooks.features.books.models.Book
import com.ykanivets.emojibooks.features.books.repository.BooksRepository

class GetBooksInteractor(
    private val booksRepository: BooksRepository = BooksRepository.instance
) {

    data class Request(
        val userId: String
    )

    sealed class Response {

        data class Success(
            val books: List<Book>
        ) : Response()

        object Failure : Response()
    }

    suspend fun execute(request: Request): Response = with(request) {
        return Response.Success(booksRepository.getAll(userId))
    }
}
