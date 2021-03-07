package com.ykanivets.emojibooks.database

import com.ykanivets.emojibooks.features.books.dao.BooksDao
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

private const val defaultJdbcUrl =
    "jdbc:postgresql://localhost:5432/emojibooks?user=emojibooks&password=emojibooks2021?reWriteBatchedInserts=true"

fun initDatabase() {
    Database.connect(hikari())
    transaction {
        create(BooksDao)
    }
}

private fun hikari() = HikariDataSource(HikariConfig().apply {
    jdbcUrl = System.getenv("JDBC_DATABASE_URL") ?: defaultJdbcUrl
})

suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction { block() }
}
