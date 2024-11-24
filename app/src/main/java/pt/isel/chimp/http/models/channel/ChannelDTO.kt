package pt.isel.chimp.http.models.channel

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

@Serializable
data class ChannelIdsDTO(
    val id: Int,
    val name: String
){
    fun toChannel() = Channel(id, name, User(0, "", ""), Visibility.PUBLIC)
}

@Serializable
data class ChannelDTO(
    val id: Int,
    val name: String,
    val visibility: String,
    val creatorId: Int
){
    fun toChannel() = Channel(id, name, User(creatorId, "", ""), Visibility.valueOf(visibility))
    //TODO fix this
}