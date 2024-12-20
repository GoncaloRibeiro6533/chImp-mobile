package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.UserEntity

class UserRepository(
    private val db: ChImpClientDB
) {

    suspend fun insertUser(users: List<User>) {
        db.userDao().insertUsers(*users.map {
            UserEntity(
                it.id,
                it.username,
                it.email
            )
        }.toTypedArray())
    }

    suspend fun getUsers(): Flow<List<User>> {
        return db.userDao().getAllUsers().map {
            it.map {
                User(
                    it.id,
                    it.username,
                    it.email
                )
            }
        }
    }

    suspend fun clear() {
        db.userDao().clear()
    }
}