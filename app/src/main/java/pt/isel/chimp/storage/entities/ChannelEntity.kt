package pt.isel.chimp.storage.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation

@Entity(
    tableName = "channel",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creatorId"],
           // onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChannelEntity(
    val id: Int,
    val name: String,
    val creatorId: Int,
    val visibility: String,
    val role: String
)

data class ChannelWithCreator(
    @Embedded val channel: ChannelEntity,
    @Relation(
        parentColumn = "creatorId",
        entityColumn = "id"
    ) val creator: UserEntity,
)

