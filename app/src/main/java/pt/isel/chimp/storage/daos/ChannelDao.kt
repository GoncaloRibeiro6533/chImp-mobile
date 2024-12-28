package pt.isel.chimp.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.ChannelWithCreator
import pt.isel.chimp.storage.entities.UserInChannel
import pt.isel.chimp.storage.entities.UserInChannelWithUser

@Dao
interface ChannelDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(vararg channels :ChannelEntity)

    @Transaction
    @Query("SELECT c.*, u.username, u.email FROM channel c JOIN user u ON c.creatorId = u.id WHERE c.id = :id;")
    fun getChannelById(id: Int): Flow<ChannelEntity>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT c.*, u.username, u.email FROM channel c JOIN user u ON c.creatorId = u.id WHERE c.name = :name;")
    fun getChannelByName(name: String): Flow<ChannelEntity>

    @Query("SELECT c.*, u.username, u.email FROM channel  c  JOIN user u ON c.creatorId = u.id")
    fun getAllChannels(): Flow<List<ChannelWithCreator>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT c.*, u.username, u.email FROM channel  c  JOIN user u ON c.creatorId = u.id JOIN user_in_channel uc ON c.id = uc.channelId")
    fun getChannels(): Flow<List<ChannelWithCreator>>

    @Query("Update channel SET name = :name WHERE id = :id")
    suspend fun updateChannelName(id: Int, name: String)

    @Query("Delete FROM channel WHERE id = :id")
    suspend fun deleteChannel(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInChannel(vararg userInChannel: UserInChannel)

    @Query("Delete FROM user_in_channel WHERE userId = :userId AND channelId = :channelId")
    suspend fun deleteUserInChannel(userId: Int, channelId: Int)

    @Transaction
    @Query("SELECT uc.userId, uc.channelId, uc.role  FROM user_in_channel uc JOIN user u ON uc.userId = u.id  WHERE uc.channelId =:channelId")
    fun getChannelMembers(channelId: Int): Flow<List<UserInChannelWithUser>>

    @Query("DELETE FROM user_in_channel")
    suspend fun clearUserInChannel()
    @Query("DELETE FROM channel")
    suspend fun clear()

}