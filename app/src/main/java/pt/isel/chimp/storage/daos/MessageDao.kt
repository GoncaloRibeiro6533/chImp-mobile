package pt.isel.chimp.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.storage.entities.MessageEntity
import pt.isel.chimp.storage.entities.MessageWithSenderAndChannel

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(vararg messages : MessageEntity)

    @Query("""
        SELECT m.*, u.username, u.email, c.name, c.visibility, u_cr.username, u_cr.email 
            FROM message m 
            JOIN user u ON m.senderId = u.id 
            JOIN user u_cr ON c.creatorId = u_cr.id
            JOIN channel c ON m.channelId = c.id 
            WHERE m.channelId = :channelId ORDER BY m.timestamp DESC;
        """)
    fun getMessagesByChannelId(channelId: Int): Flow<List<MessageWithSenderAndChannel>>

    @Query("DELETE FROM message")
    suspend fun clear()
}