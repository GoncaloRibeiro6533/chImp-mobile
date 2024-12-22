package pt.isel.chimp.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.Role
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.ChannelWithCreator
import pt.isel.chimp.storage.entities.UserInChannel

@Dao
interface ChannelDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(vararg channels :ChannelEntity)

    @Query("SELECT c.*, u.username, u.email FROM channel c JOIN user u ON c.creatorId = u.id WHERE c.id = :id;")
    fun getChannelById(id: Int): Flow<ChannelEntity>

    @Query("SELECT c.*, u.username, u.email FROM channel c JOIN user u ON c.creatorId = u.id WHERE c.name = :name;")
    fun getChannelByName(name: String): Flow<ChannelEntity>

    @Query("SELECT c.*, u.username, u.email FROM channel  c  JOIN user u ON c.creatorId = u.id;")
   // @Query("SELECT * FROM channel")
    fun getAllChannels(): Flow<List<ChannelWithCreator>>

    @Query("Update channel SET name = :name WHERE id = :id")
    suspend fun updateChannelName(id: Int, name: String)

    @Query("Delete FROM channel WHERE id = :id")
    suspend fun deleteChannel(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInChannel(vararg userInChannel: UserInChannel)

    @Query("DELETE FROM user_in_channel")
    suspend fun clearUserInChannel()
    @Query("DELETE FROM channel")
    suspend fun clear()

}