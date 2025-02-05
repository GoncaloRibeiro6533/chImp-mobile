package pt.isel.chimp.service.mock

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import io.ktor.util.date.plus
import kotlinx.coroutines.delay
import pt.isel.chimp.ChImpApplication
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.service.UserService
import pt.isel.chimp.service.mock.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

/**
 * Fake implementation of the [ProfileService] that returns a fixed profile.
 */

class MockUserService(
    private val repoMock: RepoMock,
    private val cookieStorage: CookiesStorage
    ) : UserService {

    private suspend fun <T: Any>interceptRequest(block: suspend (User) -> Either<ApiError, T>): Either<ApiError, T> {
        delay(100)
        val cookie = cookieStorage.get(Url(ChImpApplication.Companion.NGROK))[0].value
        val session = repoMock.userRepoMock.findSessionByToken(cookie) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(ApiError("User not found"))
        return block(user)
    }

    override suspend fun fetchUser(): Either<ApiError, User> =
        interceptRequest<User> { user ->
            return@interceptRequest success(user)
        }


    override suspend fun updateUsername(newUsername: String): Either<ApiError, User> =
        interceptRequest <User> { user ->
            delay(1000)
            val users = repoMock.userRepoMock.findUserByUsername(newUsername)
            if(users.any { it.username == newUsername }){
                return@interceptRequest failure(ApiError("Username already exists"))
            }
            val newUser = repoMock.userRepoMock.updateUser(user.id, newUsername)
            return@interceptRequest success(newUser)
        }

    override suspend fun login(username: String, password: String): Either<ApiError, User> {
        delay(2000)
        val user = repoMock.userRepoMock.findUserByUsername(username).firstOrNull() ?:
        return failure(ApiError("User not found"))
        repoMock.userRepoMock.findUserByPassword(user.id, password) ?:
        return failure(ApiError("Invalid password"))
        val token = repoMock.userRepoMock.createSession(user.id).token
        val cookie = Cookie(
            name = "token",
            value = token,
            path = "/",
            secure = false,
            httpOnly = true,
            domain = "localhost",
            expires = GMTDate().plus( 7 * 24 * 60 * 60 * 1000) // 7 dias
        )
        val userCookie = Cookie(
            name = "user",
            value = user.id.toString(),
            path = "/",
            secure = false,
            httpOnly = true,
            domain = "localhost",
            expires = GMTDate().plus( 7 * 24 * 60 * 60 * 1000) // 7 dias
        )
        cookieStorage.addCookie(Url(ChImpApplication.Companion.NGROK), cookie)
        cookieStorage.addCookie(Url(ChImpApplication.Companion.NGROK), userCookie)
        return success(user)
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

    override suspend fun logout() : Either<ApiError, Unit> {
            delay(1000)
            val cookie = cookieStorage.get(Url(ChImpApplication.Companion.NGROK))[0].value
            val session = repoMock.userRepoMock.findSessionByToken(cookie) ?: return failure(ApiError("Unauthorized"))
            repoMock.userRepoMock.deleteSession(session)
            return success(Unit)
    }

    override suspend fun findUserById(id: Int): Either<ApiError, User> =
        interceptRequest<User> { user ->
            delay(1000)
            val userToFind = repoMock.userRepoMock.findUserById(id)
            if (userToFind == null) return@interceptRequest failure(ApiError("User not found"))
            success(userToFind)
        }

    override suspend fun findUserByUsername(username: String): Either<ApiError, List<User>> =
        interceptRequest<List<User>> { user ->
            delay(2000)
            success(repoMock.userRepoMock.findUserByUsername(username))
        }
}
