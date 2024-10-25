package pt.isel.chimp.service

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.user.User

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
}


/**
 * Represents an error that occurred while fetching the user.
 * @property message the error message.
 * @property cause the cause of the error.
 */
class FetchUserException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UnauthorizedUserException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UserNotFoundException(message: String, cause: Throwable? = null) : Exception(message, cause)
class UsernameAlreadyExists(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Fake implementation of the [ProfileService] that returns a fixed profile.
 */

class MockUserService : UserService {

    private val users =
        mutableListOf<User>(
            User(1, "Bob", "bob@example.com"),
            User(2, "Alice", "alice@example.com"),
            User(3, "John", "john@example.com"),
        )
    private var currentId = 4
    private data class Token(val token: String, val userId: Int)
    private val sessions = mutableListOf<Token>(
        Token("token1", 1),
    )

    override suspend fun fetchUser(token: String): User {
        delay(1000)
        val session = sessions.find { it.token == token }
        if (session == null) throw FetchUserException("Unauthorized")
        val user = users.find { it.id == session.userId} ?: throw FetchUserException("User not found")
        return user
    }


    override suspend fun updateUsername(newUsername: String, token: String): User {
        delay(1000)
        val session = sessions.find { it.token == token }
        if (session == null) throw UnauthorizedUserException("Unauthorized")
        val user = users.find { it.id == session.userId } ?: throw UserNotFoundException("User not found")
        if(users.any { it.username == newUsername }) throw UsernameAlreadyExists("Username already exists")
        val newUser = user.copy(username = newUsername)
        users.remove(user)
        users.add(newUser)
        return newUser
    }
}
