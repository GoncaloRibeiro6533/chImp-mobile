package pt.isel.chimp.service.mock.repo

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import kotlin.collections.get

class ChannelRepoMock {

    companion object {
        val channels =
            mutableListOf<Channel>(
                Channel(1, "DAW", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(2, "PDM", User(2, "Alice", "alice@example.com"), Visibility.PRIVATE),
                Channel(3, "TVS long long  name", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                Channel(4, "SegInf", User(2, "Alice", "alice@example.com"), Visibility.PUBLIC),
                Channel(5, "IPW", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                Channel(6, "PG", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(7, "PSC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                Channel(8, "LSD", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(9, "LIC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
                Channel(10, "EGP", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
                Channel(11, "AED", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            )

        val userInChannel = mutableMapOf<Channel, MutableList<Pair<User, Role>>>(
            Channel(1, "DAW", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC) to mutableListOf(
                User(1, "Bob", "bob@example.com") to Role.READ_WRITE,
                User(2, "Alice", "alice@example.com") to Role.READ_WRITE,
                User(3, "Charlie", "char@emple.com") to Role.READ_ONLY,
                User(4, "David", "david@emxaple.com") to Role.READ_ONLY,
            ),
            Channel(2, "PDM", User(2, "Alice", "alice@example.com"), Visibility.PRIVATE) to mutableListOf(
                User(2, "Alice", "alice@example.com") to Role.READ_WRITE,
                User(1, "Bob", "bob@example.com") to Role.READ_ONLY,
            ),
            Channel(3, "TVS long long  name", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE) to mutableListOf(
                User(1, "Bob", "bob@example.com") to Role.READ_WRITE,
                ),
            Channel(4, "SegIngf", User(2, "Alice", "alice@example.com"), Visibility.PUBLIC) to mutableListOf(
                User(2, "Alice", "alice@example.com") to Role.READ_WRITE)
        )

        private var currentId = 50
    }

    fun addUserToChannel(userToAdd: User, channel: Channel, role: Role) {
        val entry = userInChannel.getOrDefault(channel, mutableListOf())
        entry.add(userToAdd to role)
        userInChannel[channel] = entry
    }

    fun createChannel(name: String, visibility: Visibility, creator: User): Channel {
        val channel = Channel(currentId++, name, creator, visibility)
        channels.add(channel)
        addUserToChannel(creator, channel, Role.READ_WRITE)
        return channel
    }

    fun findChannelById(id: Int): Channel? {
        return channels.find { it.id == id }
    }

    fun findChannelsOfUser(user: User): Map<Channel,Role> {
        if (!userInChannel.values.any { value-> value.any{it-> it.first.id == user.id } }) {
            return emptyMap()
        }
        val channels: Map<Channel,Role> =
            userInChannel.filter { it.value.any { it.first.id == user.id } }
                .map { it.key to it.value.find { it.first.id == user.id }!!.second }
                .toMap()
        return channels
    }


    fun findChannelByName(name: String, limit: Int = 10, skip: Int = 0 ): List<Channel> {
        val channels = channels.filter { it.name.lowercase().contains(name.lowercase()) }
        return channels//.filter { it.name.lowercase().contains(name.lowercase()) }.drop(skip).take(limit)
    }

    fun getChannelMembers(channelId: Int): List<Pair<User, Role>> {
        return userInChannel.getOrDefault(channels.find { it.id == channelId }, mutableListOf())
    }

    fun removeUser(userId: Int, channelId: Int) {
        userInChannel[channels.find { it.id == channelId }]?.removeIf { it.first.id == userId }
    }

    fun updateChannelName(channel: Channel, newName: String): Channel {
        val index = channels.indexOfFirst { it.id == channel.id }
        val newChannel = channel.copy(name = newName)
        channels[index] = newChannel
        return newChannel
    }
}