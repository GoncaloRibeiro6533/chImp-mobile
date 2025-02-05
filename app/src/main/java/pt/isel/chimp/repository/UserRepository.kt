package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.profile.ProfileScreenState
import pt.isel.chimp.repository.interfaces.UserRepositoryInterface
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.UserEntity
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.success

class UserRepository(
    private val local: ChImpClientDB,
    private val remote: ChImpService
): UserRepositoryInterface
{

    override suspend fun insertUser(users: List<User>) {
        local.userDao().insertUsers(*users.map {
            UserEntity(
                it.id,
                it.username,
                it.email
            )
        }.toTypedArray())
    }

    override fun getUsers(): Flow<List<User>> {
        return local.userDao().getAllUsers().map { user ->
            user.map {
                User(
                    it.id,
                    it.username,
                    it.email
                )
            }
        }
    }

    override suspend fun updateUser(user: User) {
        local.userDao().updateUsername(user.id, user.username)
    }

    override suspend fun changeUsername(newUsername: String): Either<ApiError, User> {
        val result = remote.userService.updateUsername(newUsername)
        when (result) {
            is Success -> {
                updateUser(result.value)
                return success(result.value)
            }
            is Failure -> return result
        }
    }

    override suspend fun fetchByUsername(username: String) =
         remote.userService.findUserByUsername(username)

    override suspend fun clear() {
        local.userDao().clear()
    }
}