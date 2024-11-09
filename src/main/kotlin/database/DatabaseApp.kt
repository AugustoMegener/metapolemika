package kito.metapolemika.database

import kito.metapolemika.reflect.ObjectRegister
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.System.getenv


object DatabaseApp {

    fun start() {
        getenv("SQL_DIALECT").let {

            val config = when (it) { "postgresql" -> 5432 to "org.postgresql.Driver"
                                     "mysql"      -> 3306 to "com.mysql.cj.jdbc.Driver"
                                     else         -> throw IllegalStateException("Unknown $it SQL dialect.") }

            Database.connect(
                "jdbc:$it://localhost:${config.first}/metapolemika",
                driver   = config.second,
                user     = getenv("DB_USER"),
                password = getenv("DB_PASSWORD"))
        }

        transaction {
            ObjectRegister.of<IdTable<*>>("TABLE").forEach {
                SchemaUtils.create(it)
                SchemaUtils.addMissingColumnsStatements(it).forEach { i -> exec(i) }
            }
        }
    }
}