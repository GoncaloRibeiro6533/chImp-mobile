package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.utils.Either

interface UserRepositoryInterface {
    suspend fun changeUsername(newUsername: String): Either<ApiError, User>
    fun getUsers(): Flow<List<User>>
    suspend fun insertUser(users: List<User>)
    suspend fun updateUser(user: User)
    suspend fun clear()
    suspend fun fetchByUsername(username: String): Either<ApiError, List<User>>
}