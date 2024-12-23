package pt.isel.chimp.storage.entities
/*
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation

@Entity(
    tableName = "invitations",
    primaryKeys = ["invitationId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["receiverId"],
        ),
        ForeignKey(
            entity = ChannelEntity::class,
            parentColumns = ["id"],
            childColumns = ["channelId"],
        )
    ]
    )
data class InvitationEntity(
    val invitationId: Int,
    val senderId: Int,
    val receiverId: Int,
    val channelId: Int,
    val roleI: String,
    val isUsed: Boolean,
    val timestamp: Long,
    )


data class InvitationWithSenderChannel(
    @Embedded val invitation: InvitationEntity,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "id"
    ) val sender: UserEntity,
    @Embedded val channel: ChannelEntity,
    @Relation(
        parentColumn = "creatorId",
        entityColumn = "id"
    ) val creator: UserEntity,
)*/