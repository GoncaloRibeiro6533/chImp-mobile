package pt.isel.chimp.channels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

@Parcelize
data class UserParcelable(
    val id: Int,
    val name: String,
    val email: String
): Parcelable {
    fun toUser() = User(id, name, email)
}


@Parcelize
data class ChannelParcelable(
    val id: Int,
    val name: String,
    val creator: UserParcelable,
    val visibility: Visibility,
    val role: Role
): Parcelable {
    fun toChannel() = Channel(id, name, creator.toUser(), visibility)
}
