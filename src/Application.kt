package com.ykanivets.emojibooks

import com.google.firebase.FirebaseApp
import com.ykanivets.emojibooks.database.initDatabase
import com.ykanivets.emojibooks.features.books.books
import firebase.FirebasePrincipal
import firebase.firebase
import firebase.initFirebase
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.slf4j.event.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    initFirebase()
    initDatabase()

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    authentication {
        firebase("firebase", FirebaseApp.getInstance()) {
            validate { credential ->
                FirebasePrincipal(userId = credential.token.uid)
            }
        }
    }

    routing {
        authenticate("firebase") {
            books()
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

