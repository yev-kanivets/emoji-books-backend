package com.ykanivets.emojibooks.features.books

import com.ykanivets.emojibooks.features.books.models.Book
import com.ykanivets.emojibooks.features.books.models.BookBody
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.books() = route("books") {

    val books = mutableListOf<Book>()

    get {
        call.respond(HttpStatusCode.OK, books)
    }

    post {
        val newBook = call.receive<BookBody>()
        books.add(newBook.toBook(id = UUID.randomUUID().toString()))
        call.respond(HttpStatusCode.OK, books)
    }

    put("/{id}") {
        val bookId = call.parameters["id"]
        val bookBody = call.receive<BookBody>()

        books.replaceAll { book -> if (book.id == bookId) bookBody.toBook(id = bookId) else book }

        call.respond(HttpStatusCode.OK, books)
    }

    delete("/{id}") {
        val bookId = call.parameters["id"]
        books.removeIf { book -> book.id == bookId }
        call.respond(HttpStatusCode.OK, books)
    }
}
