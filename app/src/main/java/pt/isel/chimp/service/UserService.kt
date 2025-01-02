package pt.isel.chimp.service

import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.utils.Either


/**
 * Contract of the service that provides the user.
 */
interface UserService {
    /**
     * Fetches a user given a token.
     * @return the user.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun fetchUser(): Either<ApiError, User>

    /**
     * Updates the username of a user.
     * @param newUsername the new username.
     * @return the updated user.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun updateUsername(newUsername: String): Either<ApiError, User>

    /**
     * Logs in a user.
     * @param username the username.
     * @param password the password.
     * @return the authenticated user.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun login(username: String, password: String): Either<ApiError, User>

    /**
     * Registers a user.
     * @param username the username.
     * @param password the password.
     * @param email the email.
     * @return the registered user.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun register(username: String, password: String, email: String): Either<ApiError, User>

    /**
     * Finds a user by its id.
     * @param id of an user.
     * @return the user.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun findUserById(id: Int): Either<ApiError, User>

    /**
     * Logs out a user.
     * @return Unit.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun logout(): Either<ApiError, Unit>


    suspend fun findUserByUsername(query: String): Either<ApiError, List<User>>
}