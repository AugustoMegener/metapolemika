package kito.metapolemika.database

import kito.metapolemika.reflect.ObjectRegister
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.System.getenv


object DatabaseApp {

    fun start() {
        Database.connect("jdbc:postgresql://localhost:5432/metapolemika", driver = "org.postgresql.Driver",
            user = getenv("DB_USER"), password = getenv("DB_PASSWORD"))

        transaction {
            ObjectRegister.of<IdTable<*>>("TABLE").forEach {
                SchemaUtils.create(it)
                SchemaUtils.addMissingColumnsStatements(it).forEach { i -> exec(i) }
            }
        }
    }
}