package com.ykanivets.emojibooks.features.books.interactors

import com.ykanivets.emojibooks.features.books.models.Book
import com.ykanivets.emojibooks.features.books.repository.BooksRepository

class GetBooksInteractor(
    private val booksRepository: BooksRepository = BooksRepository.instance
) {

    sealed class Response {

        data class Success(
            val books: List<Book>
        ) : Response()

        object Failure : Response()
    }

    suspend fun execute(): Response {
        return Response.Success(booksRepository.getAll())
    }
}
