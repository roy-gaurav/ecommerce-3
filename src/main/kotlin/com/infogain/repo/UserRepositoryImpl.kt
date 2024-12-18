package com.infogain.repo

import com.infogain.models.User
import com.infogain.responsepayload.UserResponsePayload
import com.infogain.tables.UsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
class UserRepositoryImpl : UserRepository {
    override fun getAllUsers(): List<User> = transaction {
        UsersTable.selectAll().map {
            it.toUser()
        }
    }

   override fun getUserById(id: Int): User? = transaction {
        //UsersTable.select { UsersTable.id eq id }.singleOrNull()

       // select * from users where id =?(parameter id value)
       UsersTable.select(UsersTable.id eq id ).
       map { it.toUser() }.singleOrNull()
    }

    private fun ResultRow.toUser() = User(
        id = this[UsersTable.id],
        name = this[UsersTable.name],
        email = this[UsersTable.email],
        password = this[UsersTable.password],
        role = this[UsersTable.role],
        createdAt = this[UsersTable.createdAt],
        updatedAt = this[UsersTable.updatedAt]
    )

    override fun createuser(user: User):Int = transaction{
        UsersTable.insert { it[name] = user.name
        it[email] = user.email
        it[password] = user.password
        it[role] = user.role
        it[createdAt] = System.currentTimeMillis().toString()
        it[updatedAt] = System.currentTimeMillis().toString()
        }[UsersTable.id]
    }
}