package com.ykanivets.emojibooks.features.books

import com.ykanivets.emojibooks.features.books.models.BookBody
import com.ykanivets.emojibooks.features.books.repository.BooksRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.books(
    booksRepository: BooksRepository = BooksRepository()
) = route("books") {

    get {
        call.respond(HttpStatusCode.OK, booksRepository.getAll())
    }

    post {
        val newBook = call.receive<BookBody>()
        booksRepository.insert(newBook.toBook(id = UUID.randomUUID().toString()))
        call.respond(HttpStatusCode.OK, booksRepository.getAll())
    }

    put("/{id}") {
        val bookId = call.parameters["id"]!!
        val bookBody = call.receive<BookBody>()

        booksRepository.update(bookBody.toBook(id = bookId))

        call.respond(HttpStatusCode.OK, booksRepository.getAll())
    }

    delete("/{id}") {
        val bookId = call.parameters["id"]!!
        booksRepository.delete(bookId)
        call.respond(HttpStatusCode.OK, booksRepository.getAll())
    }
}
