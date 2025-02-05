package pt.isel.chimp.storage.daos
/*
import androidx.room.Dao
import androidx.room.Query
import pt.isel.chimp.storage.entities.ConnectionEntity


@Dao
interface ConnectionDao {

    @Query("INSERT INTO connection (started, lost, checked) VALUES (:started, 0, 0)")
    suspend fun createConnection(started: Long)

    @Query("SELECT * FROM connection WHERE  id = (SELECT MIN(id) FROM connection WHERE checked = 0);")
    suspend fun getLastConnection(): ConnectionEntity?

    @Query("Update connection SET lost = 1 WHERE id = :id")
    suspend fun updateConnectionLost(id: Int)

    @Query("Update connection SET checked = 1 WHERE id = :id")
    suspend fun markAsChecked(id: Int)

    @Query("Delete FROM connection")
    suspend fun clear()
}*/