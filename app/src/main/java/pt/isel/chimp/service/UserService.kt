package pt.isel.chimp.service

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.user.AuthenticatedUser
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
    suspend fun login(username: String, password: String): AuthenticatedUser
    suspend fun register(username: String, password: String, email: String): AuthenticatedUser
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

class MockUserService : UserService {

    private val users =
        mutableListOf<User>(
            User(1, "Bob", "bob@example.com"),
            User(2, "Alice", "alice@example.com"),
            User(3, "John", "john@example.com"),
        )
    private val passwords = mutableMapOf(
        1 to "A1234ab",
        2 to "1234VDd",
        3 to "1234SADfs",
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
        if(users.any { it.username == newUsername }) throw UsernameAlreadyExistsException("Username already exists")
        val newUser = user.copy(username = newUsername)
        users.remove(user)
        users.add(newUser)
        return newUser
    }

    override suspend fun login(username: String, password: String): AuthenticatedUser {
        delay(1000)
        val user = users.find { it.username == username } ?: throw FetchUserException("No user corresponding to $username")
        if(passwords[user.id] != password) throw InvalidPasswordException("Invalid password")
        val token = "token${user.id}"
        sessions.add(Token(token, user.id))
        return AuthenticatedUser(user, token)
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): AuthenticatedUser {
        delay(1000)
        val nameTaken = users.find { it.username == username } != null
        if (nameTaken) throw UsernameAlreadyExistsException("Username already exists")
        val emailTaken = users.find { it.email == email } != null
        if (emailTaken) throw EmailAlreadyRegisteredException("This email is already registered")
        val user = User(currentId, username, email)
        val token = "token${currentId++}"
        users.add(user)
        passwords[user.id] = password
        return AuthenticatedUser(user, token)
    }

    override suspend fun logout(token: String) {
        val session = sessions.find { it.token == token } ?: throw UnauthorizedUserException("Unauthorized")
        sessions.removeIf { it.token == session.token }
    }
}
