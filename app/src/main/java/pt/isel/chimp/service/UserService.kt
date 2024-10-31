package pt.isel.chimp.service

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

/**
 * Contract of the service that provides the user profile.
 * @return the user profile.
 * @throws UserServiceException if an error occurs while fetching the user.
 * @throws UserServiceUnauthorizedException if the user is not authorized to fetch the profile.
 * @throws UserServiceNotFoundException if the profile is not found.
 * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
 */

interface UserService {
    suspend fun fetchUser(token: String): Either<UserError, User>
    suspend fun updateUsername(newUsername: String, token: String): Either<UserError, User>
    suspend fun login(username: String, password: String): Either<UserError, AuthenticatedUser>
    suspend fun register(username: String, password: String, email: String): Either<UserError, User>
    suspend fun findUserById(id: Int): Either<UserError, User>
    suspend fun logout(token: String): Unit
}


/**
 * Represents an error that occurred while fetching the user.
 * @property message the error message.
 * @property cause the cause of the error.
 */
class FetchUserException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Represents an error that occurred while updating the user profile.
 * @property message the error message.
 * @property cause the cause of the error.
 */
class UnauthorizedUserException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Represents an error that
 * @property message the error message.
 * @property cause the cause of the error.
 */

sealed class UserError(val message: String) {
    data object UnauthorizedUserException : UserError("Unauthorized")
    data object UserNotFoundException : UserError("User not found")
    data object UsernameAlreadyExistsException : UserError("")
    data object InvalidPasswordException : UserError("")
    data object EmailAlreadyRegisteredException : UserError("")

}


/**
 * Fake implementation of the [ProfileService] that returns a fixed profile.
 */

class MockUserService(private val repoMock: RepoMock) : UserService {


    override suspend fun fetchUser(token: String): Either<UserError, User> {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: return failure(UserError.UnauthorizedUserException)
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(UserError.UserNotFoundException)
        return success(user)
    }


    override suspend fun updateUsername(newUsername: String, token: String): Either<UserError, User> {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: return failure(UserError.UnauthorizedUserException)
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(UserError.UserNotFoundException)
        if(repoMock.userRepoMock.findUserByUsername(newUsername).any { it.username == newUsername }){
            return failure(UserError.UsernameAlreadyExistsException)
        }
        val newUser = repoMock.userRepoMock.updateUser(user.id, newUsername)
        return success(newUser)
    }

    override suspend fun login(username: String, password: String): Either<UserError, AuthenticatedUser> {
        delay(2000)
        val user = repoMock.userRepoMock.findUserByUsername(username).firstOrNull() ?: return failure(UserError.UserNotFoundException)
        repoMock.userRepoMock.findUserByPassword(user.id, password) ?: return failure(UserError.InvalidPasswordException)
        val token = repoMock.userRepoMock.createSession(user.id).token
        return success(AuthenticatedUser(user, token))
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): Either<UserError, User> {
        delay(1000)
        val nameTaken = repoMock.userRepoMock.findUserByUsername(username).any { it.username == username }
        if (nameTaken) return failure(UserError.UsernameAlreadyExistsException)
        val emailTaken =repoMock.userRepoMock.findByEmail(email)
        if (emailTaken != null) return failure(UserError.EmailAlreadyRegisteredException)
        val user = repoMock.userRepoMock.createUser(username, email, password)
        return success(user)
    }

    override suspend fun logout(token: String) {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: throw FetchUserException("Unauthorized")
        repoMock.userRepoMock.deleteSession(session)
    }

    override suspend fun findUserById(id: Int): Either<UserError, User> {
        delay(500)
        val user = repoMock.userRepoMock.findUserById(id) ?: return failure(UserError.UserNotFoundException)
        return success(user)
    }
}
