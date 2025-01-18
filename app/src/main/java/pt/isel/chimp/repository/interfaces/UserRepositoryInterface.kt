package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.user.User

interface UserRepositoryInterface {
    fun getUsers(): Flow<List<User>>
    suspend fun insertUser(users: List<User>)
    suspend fun updateUser(user: User)
    suspend fun clear()

}