package com.ykanivets.emojibooks.features.books

import com.ykanivets.emojibooks.features.books.interactors.AddBookInteractor
import com.ykanivets.emojibooks.features.books.interactors.DeleteBookInteractor
import com.ykanivets.emojibooks.features.books.interactors.GetBooksInteractor
import com.ykanivets.emojibooks.features.books.interactors.UpdateBookInteractor
import com.ykanivets.emojibooks.features.books.models.BookBody
import firebase.FirebasePrincipal
import io.ktor.application.*
import io.ktor.auth.*
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
        val userId = call.principal<FirebasePrincipal>()!!.userId
        call.respondBooks(getBooksInteractor, userId)
    }

    post {
        val userId = call.principal<FirebasePrincipal>()!!.userId
        val newBook = call.receive<BookBody>()

        val request = AddBookInteractor.Request(userId, newBook)
        when (addBookInteractor.execute(request)) {
            is AddBookInteractor.Response.Success -> call.respondBooks(getBooksInteractor, userId)
            is AddBookInteractor.Response.Failure -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    put("/{id}") {
        val userId = call.principal<FirebasePrincipal>()!!.userId
        val bookId = call.parameters["id"]!!
        val bookBody = call.receive<BookBody>()

        val request = UpdateBookInteractor.Request(userId, bookId, bookBody)
        when (updateBookInteractor.execute(request)) {
            is UpdateBookInteractor.Response.Success -> call.respondBooks(getBooksInteractor, userId)
            is UpdateBookInteractor.Response.Failure -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    delete("/{id}") {
        val userId = call.principal<FirebasePrincipal>()!!.userId
        val bookId = call.parameters["id"]!!

        val request = DeleteBookInteractor.Request(userId, bookId)
        when (deleteBookInteractor.execute(request)) {
            is DeleteBookInteractor.Response.Success -> call.respondBooks(getBooksInteractor, userId)
            is DeleteBookInteractor.Response.Failure -> call.respond(HttpStatusCode.BadRequest)
        }
    }
}

private suspend fun ApplicationCall.respondBooks(
    getBooksInteractor: GetBooksInteractor,
    userId: String,
) {
    val request = GetBooksInteractor.Request(userId)
    when (val response = getBooksInteractor.execute(request)) {
        is GetBooksInteractor.Response.Success -> respond(HttpStatusCode.OK, response.books)
        is GetBooksInteractor.Response.Failure -> respond(HttpStatusCode.BadRequest)
    }
}