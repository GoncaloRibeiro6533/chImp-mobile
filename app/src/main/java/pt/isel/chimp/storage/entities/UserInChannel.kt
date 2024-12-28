package pt.isel.chimp.storage.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation

@Entity(
    tableName = "user_in_channel",
    primaryKeys = ["userId", "channelId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            //onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChannelEntity::class,
            parentColumns = ["id"],
            childColumns = ["channelId"],
            //onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class UserInChannel(
    val userId: Int,
    val channelId: Int,
    val role: String,
)


data class UserInChannelWithUser(
    @Embedded val userInChannel: UserInChannel,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    ) val user: UserEntity,
)