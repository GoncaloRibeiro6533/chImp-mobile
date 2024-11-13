package pt.isel.chimp.service.repo

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.service.repo.UserRepoMock.Companion.users

class ChannelRepoMock {

    companion object {
        val channels =
            mutableListOf<Channel>(
                Channel(1, "DAW", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(2, "PDM", User(2, "Alice", "alice@example.com"), Visibility.PRIVATE),
                Channel(3, "TVS long long  name", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                /*Channel(4, "SegIngf", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(5, "IPW", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                Channel(6, "PG", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(7, "PSC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                Channel(8, "LSD", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(9, "LIC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                Channel(10, "EGP", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(11, "AED", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),

                 */
                Channel(12, "teste", users[1], Visibility.PRIVATE),
            )

        val userInChannel = mutableListOf<UserInChannel>(
            UserInChannel(1, 1, Role.READ_WRITE),
            UserInChannel(2, 1, Role.READ_WRITE),
            UserInChannel(2, 2, Role.READ_ONLY),
            UserInChannel(1, 2, Role.READ_WRITE),
            UserInChannel(1, 3, Role.READ_WRITE),
            /*UserInChannel(1, 4, Role.READ_WRITE),
            UserInChannel(1, 5, Role.READ_WRITE),
            UserInChannel(1, 6, Role.READ_ONLY),
            UserInChannel(1, 7, Role.READ_WRITE),
            UserInChannel(1, 8, Role.READ_WRITE),
            UserInChannel(1, 9, Role.READ_WRITE),
            UserInChannel(1, 10, Role.READ_WRITE),
            UserInChannel(1, 11, Role.READ_WRITE),
            */


        )

        private var currentId = 50
    }

    fun addUserToChannel(userToAdd: Int, channel: Channel, role: Role) {
        userInChannel.add(UserInChannel(userToAdd, channel.id, role))
    }

    fun createChannel(name: String, visibility: Visibility, creator: User): Channel {
        val channel = Channel(currentId++, name, creator, visibility)
        channels.add(channel)
        addUserToChannel(creator.id, channel, Role.READ_WRITE)
        return channel
    }

    fun findChannelById(id: Int): Channel? {
        return channels.find { it.id == id }
    }

    fun findChannelsOfUser(user: User): List<Channel> {
        return userInChannel
            .filter { it.userId == user.id }
            .mapNotNull { userInChannel -> channels.find { it.id == userInChannel.channelId } }
    }

    fun findChannelByName(name: String, limit: Int = 10, skip: Int = 0 ): List<Channel> {
        return channels.filter { it.name.contains(name) }.drop(skip).take(limit)
    }

    fun getChannelMembers(channel: Channel): List<UserInChannel> {
        return  userInChannel.filter { it.channelId == channel.id }
    }
}