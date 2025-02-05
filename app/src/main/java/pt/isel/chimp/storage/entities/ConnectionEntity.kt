package pt.isel.chimp.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "connection")
data class ConnectionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val started: Long,
    val lost: Boolean = false,
    val checked: Boolean = false
)
