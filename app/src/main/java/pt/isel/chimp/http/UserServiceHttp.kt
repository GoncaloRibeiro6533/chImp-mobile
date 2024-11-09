package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.models.user.AuthenticatedUserDTO
import pt.isel.chimp.http.models.user.UserDTO
import pt.isel.chimp.http.models.user.UserIdentifiersDTO
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

    override suspend fun fetchUser(token: String): Either<ApiError, User> {
        TODO()
    }

    override suspend fun updateUsername(
        newUsername: String,
        token: String
    ): Either<ApiError, User> {
        return when(val response = client.put<UserDTO>(
            url = "/user/edit/username",
            token = token,
            body = UsernameUpdateInput(newUsername))) {
            is Success -> success(response.value.toUser())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ): Either<ApiError, AuthenticatedUser> {
        return when(val response = client.post<AuthenticatedUserDTO>(
            url = "/user/login",
            body = UserLoginCredentialsInput(username = username, password = password))) {
            is Success -> success(response.value.toAuthenticatedUser())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): Either<ApiError, User> =
        when (val response  = client.post<UserDTO>(
            url = "/user/register",
            body = UserRegisterInput(username, email, password))) {
            is Success -> success(response.value.toUser())
            is Failure -> failure(response.value)
        }


    override suspend fun findUserById(token: String, id: Int): Either<ApiError, User> {
        return when(val response = client.get< UserIdentifiersDTO>("/user/$id", "DfmMD7TH09TTsR88_Cs6IO-TivmrCJEMNOzM1Isy4u4=")) {
            is  Success -> success(response.value.toUser())
            is  Failure -> failure(response.value)
        }

    }


    override suspend fun logout(token: String): Either<ApiError, Unit> {
        return when(val response = client.post<Unit>(
            url = "/user/logout",
            token = token)) {
            is Success -> success(Unit)
            is Failure -> failure(response.value)
        }
    }

}