package pt.isel.chimp.storage.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import pt.isel.chimp.domain.Role

/*
@Entity(
    tableName = "user_channel_role",
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
            //onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserChannelRoleEntity(
    val userId: Int,
    val channelId: Int,
    val role: Role

)*/
