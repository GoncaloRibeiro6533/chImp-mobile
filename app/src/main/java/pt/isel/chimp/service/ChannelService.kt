package pt.isel.chimp.service

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success


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
    suspend fun createChannel(creatorToken: String, name: String, visibility: Visibility): Either<ApiError, Channel>

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
    suspend fun getChannelsByUser(token: String, limit: Int = 10, skip: Int = 0): Either<ApiError, List<Channel>>

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
    suspend fun addUserToChannel(token: String, userToAdd: Int, channelId: Int, role: Role): Either<ApiError, Channel>

    /**
     * Gets the members of a channel.
     * @param token the user token.
     * @param channel the channel.
     * @return the members of the channel.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun getChannelMembers(token: String, channel: Channel): Either<ApiError, List<Pair<User, UserInChannel>>>

    /**
     * Creates a channel.
     * @param token the user token.
     * @param channelId the channel id.
     * @param userId the user id.
     * @return the created channel.
     * @return ApiError if an error occurs.
     * @throws kotlin.coroutines.CancellationException if the operation was cancelled.
     */
    suspend fun removeUserFromChannel(token: String, channelId: Int, userID: Int): Either<ApiError, Channel>
}


