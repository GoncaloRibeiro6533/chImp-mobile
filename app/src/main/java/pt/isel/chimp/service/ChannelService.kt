package pt.isel.chimp.service

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.utils.Either
import kotlinx.coroutines.CancellationException

/**
 * Contract of the service that provides channels.
*/

interface ChannelService {
    /**
     * Creates a channel.
     * @param creatorToken the user token.
     * @param name the channel name.
     * @param visibility the channel visibility.
     * @return the created channel.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun createChannel(
        name: String,
        creatorId: Int,
        visibility: Visibility,
        creatorToken: String
    ): Either<ApiError, Channel>

    /**
     * Gets a channel by its id.
     * @param id the channel id.
     * @param token the user token.
     * @return the channel.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun getChannelById(id: Int, token: String): Either<ApiError, Channel>

    /**
     * Gets the channels of a user.
     * @param token the user token.
     * @param limit the maximum number of channels to return.
     * @param skip the number of channels to skip.
     * @return the channels.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun getChannelsOfUser(userId: Int, limit: Int = 10, skip: Int = 0, token: String): Either<ApiError, List<Channel>>

    /**
     * Adds a user to a channel.
     * @param token the user token.
     * @param userToAdd the user to add.
     * @param channelId the channel id.
     * @param role the role of the user in the channel.
     * @return the channel.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun joinChannel(token: String, userToAdd: Int, channelId: Int, role: Role): Either<ApiError, Channel>

    /**
     * Gets the members of a channel.
     * @param token the user token.
     * @param channelId the channel.
     * @return the members of the channel.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun getChannelMembers(token: String, channelId: Int): Either<ApiError, List<Pair<User, Role>>>

    suspend fun updateChannelName(token: String, channelId: Int, newName: String): Either<ApiError, Channel>

    /**
     * Creates a channel.
     * @param token the user token.
     * @param channelId the channel id.
     * @return the created channel.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun removeUserFromChannel(token: String, channelId: Int, userID: Int): Either<ApiError, Channel>
}


