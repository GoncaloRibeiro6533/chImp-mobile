package pt.isel.chimp.storage.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation


@Entity(
    tableName = "message",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChannelEntity::class,
            parentColumns = ["id"],
            childColumns = ["channelId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    )
data class MessageEntity(
    val id: Int,
    val senderId: Int,
    val channelId: Int,
    val content: String,
    val timestamp: Long
)



data class MessageWithSenderAndChannel(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "id"
    ) val sender: UserEntity,
)