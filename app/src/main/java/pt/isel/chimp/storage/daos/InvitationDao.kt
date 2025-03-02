package pt.isel.chimp.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.storage.entities.InvitationEntity
import pt.isel.chimp.storage.entities.InvitationWithSenderChannel


@Dao
interface InvitationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvitations(vararg invitation: InvitationEntity)

    @Query("DELETE FROM invitations WHERE invitationId = :invitationId")
    suspend fun deleteInvitation(invitationId: Int)

    @Query("SELECT * FROM invitations JOIN user ON invitations.senderId = user.id JOIN channel ON invitations.channelId = channel.id JOIN user creator ON channel.creatorId = creator.id")
    fun getAllInvitations(): Flow<List<InvitationWithSenderChannel>>

    @Query("Delete from invitations")
    suspend fun deleteAllInvitations()

    @Query("SELECT COUNT(*) FROM invitations")
    suspend fun hasInvitations(): Int

}

