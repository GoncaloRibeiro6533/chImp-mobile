package pt.isel.chimp.service.mock

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.UserService
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

/**
 * Fake implementation of the [ProfileService] that returns a fixed profile.
 */

class MockUserService(private val repoMock: RepoMock) : UserService {


    override suspend fun fetchUser(token: String): Either<ApiError, User> {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(ApiError("User not found"))
        return success(user)
    }


    override suspend fun updateUsername(newUsername: String, token: String): Either<ApiError, User> {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(ApiError("User not found"))
        if(repoMock.userRepoMock.findUserByUsername(newUsername).any { it.username == newUsername }){
            return failure(ApiError("Username already exists"))
        }
        val newUser = repoMock.userRepoMock.updateUser(user.id, newUsername)
        return success(newUser)
    }

    override suspend fun login(username: String, password: String): Either<ApiError, AuthenticatedUser> {
        delay(2000)
        val user = repoMock.userRepoMock.findUserByUsername(username).firstOrNull() ?:
        return failure(ApiError("User not found"))
        repoMock.userRepoMock.findUserByPassword(user.id, password) ?:
        return failure(ApiError("Invalid password"))
        //repoMock.userRepoMock.deleteAllSessions()
        val token = repoMock.userRepoMock.createSession(user.id).token
        return success(AuthenticatedUser(user, token))
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): Either<ApiError, User> {
        delay(1000)
        val nameTaken = repoMock.userRepoMock.findUserByUsername(username).any { it.username == username }
        if (nameTaken) return failure(ApiError("Username already exists"))
        val emailTaken =repoMock.userRepoMock.findByEmail(email)
        if (emailTaken != null) return failure(ApiError("Email already registered"))
        val user = repoMock.userRepoMock.createUser(username, email, password)
        return success(user)
    }

    override suspend fun logout(token: String) : Either<ApiError, Unit> {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: return failure(ApiError("Unauthorized"))
        return success(repoMock.userRepoMock.deleteSession(session))
    }

    override suspend fun findUserById(token: String, id: Int): Either<ApiError, User> {
        delay(500)
        if (id < 0) return failure(ApiError("Invalid id"))
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(id) ?: return failure(ApiError("User not found"))
        return success(user)
    }
}
