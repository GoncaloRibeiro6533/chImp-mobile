package pt.isel.chimp.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.storage.entities.MessageEntity
import pt.isel.chimp.storage.entities.MessageWithSenderAndChannel

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(vararg messages : MessageEntity)

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT m.*, u.username, u.email, c.name, c.visibility FROM message m JOIN user u ON m.senderId = u.id JOIN channel c ON m.channelId=c.id WHERE m.channelId = :channelId ORDER BY m.timestamp DESC;")
    fun getMessagesByChannelId(channelId: Int): Flow<List<MessageWithSenderAndChannel>>

    @Query("DELETE FROM message")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM message WHERE channelId =:channelId")
    suspend fun nMessagesOfChannel(channelId: Int): Int

    @Query("DELETE FROM message WHERE channelId = :channelId")
    suspend fun deleteMessagesOfChannel(channelId: Int)
}