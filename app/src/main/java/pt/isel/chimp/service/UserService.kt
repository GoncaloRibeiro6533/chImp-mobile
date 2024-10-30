package pt.isel.chimp.service

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.service.repo.UserRepoMock

/**
 * Contract of the service that provides the user profile.
 * @return the user profile.
 * @throws UserServiceException if an error occurs while fetching the user.
 * @throws UserServiceUnauthorizedException if the user is not authorized to fetch the profile.
 * @throws UserServiceNotFoundException if the profile is not found.
 * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
 */

interface UserService {
    suspend fun fetchUser(token: String): User
    suspend fun updateUsername(newUsername: String, token: String): User
    suspend fun login(username: String, password: String): AuthenticatedUser
    suspend fun register(username: String, password: String, email: String): User
    suspend fun findUserById(id: Int): User
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
class UserNotFoundException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UsernameAlreadyExistsException(message: String, cause: Throwable? = null) : Exception(message, cause)
class InvalidPasswordException(message: String, cause: Throwable? = null) : Exception(message, cause)
class EmailAlreadyRegisteredException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Fake implementation of the [ProfileService] that returns a fixed profile.
 */

class MockUserService(private val repoMock: RepoMock) : UserService {


    override suspend fun fetchUser(token: String): User {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: throw FetchUserException("Unauthorized")
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: throw FetchUserException("User not found")
        return user
    }


    override suspend fun updateUsername(newUsername: String, token: String): User {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: throw FetchUserException("Unauthorized")
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: throw FetchUserException("User not found")
        if(repoMock.userRepoMock.findUserByUsername(newUsername).any { it.username == newUsername }) throw UsernameAlreadyExistsException("Username already exists")
        val newUser = repoMock.userRepoMock.updateUser(user.id, newUsername)
        return newUser
    }

    override suspend fun login(username: String, password: String): AuthenticatedUser {
        delay(2000)
        val user = repoMock.userRepoMock.findUserByUsername(username).firstOrNull() ?: throw UserNotFoundException("User not found")
        repoMock.userRepoMock.findUserByPassword(user.id, password) ?: throw InvalidPasswordException("Invalid password")
        val token = repoMock.userRepoMock.createSession(user.id).token
        return AuthenticatedUser(user, token)
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): User {
        delay(1000)
        val nameTaken = repoMock.userRepoMock.findUserByUsername(username).any { it.username == username }
        if (nameTaken) throw UsernameAlreadyExistsException("This username is already taken")
        val emailTaken =repoMock.userRepoMock.findByEmail(email)
        if (emailTaken != null) throw EmailAlreadyRegisteredException("This email is already registered")
        val user = repoMock.userRepoMock.createUser(username, email, password)
        return user
    }

    override suspend fun logout(token: String) {
        delay(1000)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: throw FetchUserException("Unauthorized")
        repoMock.userRepoMock.deleteSession(session)
    }

    override suspend fun findUserById(id: Int): User {
        delay(500)
        return repoMock.userRepoMock.findUserById(id) ?: throw UserNotFoundException("User not found")
    }
}
