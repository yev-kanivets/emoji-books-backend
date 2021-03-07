package com.ykanivets.emojibooks.features.books

import com.ykanivets.emojibooks.features.books.interactors.AddBookInteractor
import com.ykanivets.emojibooks.features.books.interactors.DeleteBookInteractor
import com.ykanivets.emojibooks.features.books.interactors.GetBooksInteractor
import com.ykanivets.emojibooks.features.books.interactors.UpdateBookInteractor
import com.ykanivets.emojibooks.features.books.models.BookBody
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.books(
    getBooksInteractor: GetBooksInteractor = GetBooksInteractor(),
    addBookInteractor: AddBookInteractor = AddBookInteractor(),
    updateBookInteractor: UpdateBookInteractor = UpdateBookInteractor(),
    deleteBookInteractor: DeleteBookInteractor = DeleteBookInteractor()
) = route("books") {

    get {
        call.respondBooks(getBooksInteractor)
    }

    post {
        val newBook = call.receive<BookBody>()

        val request = AddBookInteractor.Request(newBook)
        when (addBookInteractor.execute(request)) {
            is AddBookInteractor.Response.Success -> call.respondBooks(getBooksInteractor)
            is AddBookInteractor.Response.Failure -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    put("/{id}") {
        val bookId = call.parameters["id"]!!
        val bookBody = call.receive<BookBody>()

        val request = UpdateBookInteractor.Request(bookId, bookBody)
        when (updateBookInteractor.execute(request)) {
            is UpdateBookInteractor.Response.Success -> call.respondBooks(getBooksInteractor)
            is UpdateBookInteractor.Response.Failure -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    delete("/{id}") {
        val bookId = call.parameters["id"]!!

        val request = DeleteBookInteractor.Request(bookId)
        when (deleteBookInteractor.execute(request)) {
            is DeleteBookInteractor.Response.Success -> call.respondBooks(getBooksInteractor)
            is DeleteBookInteractor.Response.Failure -> call.respond(HttpStatusCode.BadRequest)
        }
    }
}

private suspend fun ApplicationCall.respondBooks(
    getBooksInteractor: GetBooksInteractor
) {
    when (val response = getBooksInteractor.execute()) {
        is GetBooksInteractor.Response.Success -> respond(HttpStatusCode.OK, response.books)
        is GetBooksInteractor.Response.Failure -> respond(HttpStatusCode.BadRequest)
    }
}