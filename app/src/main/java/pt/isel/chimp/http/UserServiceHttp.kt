package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.models.user.UserDTO
import pt.isel.chimp.http.models.user.UserLoginCredentialsInput
import pt.isel.chimp.http.models.user.UserRegisterInput
import pt.isel.chimp.http.models.user.UsernameUpdateInput
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.get
import pt.isel.chimp.http.utils.post
import pt.isel.chimp.http.utils.put
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class UserServiceHttp(private val client: HttpClient) : UserService {

    override suspend fun fetchUser(): Either<ApiError, User> {
        TODO()
    }

    override suspend fun updateUsername(
        newUsername: String,
    ): Either<ApiError, User> {
        return when(val response = client.put<UserDTO>(
            url = "/user/edit/username",
            body = UsernameUpdateInput(newUsername))) {
            is Success -> success(response.value.toUser())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ): Either<ApiError, User> {
        return when(val response = client.post<UserDTO>(
            url = "/user/login",
            body = UserLoginCredentialsInput(username = username, password = password))) {
            is Success -> success(response.value.toUser())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): Either<ApiError, User> =
        when (val response  = client.post<UserDTO>(
            url = "/user/pdm/register",
            body = UserRegisterInput(username, email, password))) {
            is Success -> success(response.value.toUser())
            is Failure -> failure(response.value)
        }


    override suspend fun findUserById(id: Int): Either<ApiError, User> {
        return when(val response = client.get<UserDTO>("/user/$id")) {
            is  Success -> success(response.value.toUser())
            is  Failure -> failure(response.value)
        }
    }

    override suspend fun logout(): Either<ApiError, Unit> {
        return when(val response = client.post<Unit>(
            url = "/user/logout")) {
            is Success -> success(Unit)
            is Failure -> failure(response.value)
        }
    }
}