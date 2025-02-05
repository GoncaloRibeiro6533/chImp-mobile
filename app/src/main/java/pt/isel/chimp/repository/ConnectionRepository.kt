package pt.isel.chimp.repository
/*
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.ConnectionEntity*/
/*
class ConnectionRepository(
    private val db: ChImpClientDB
) {

    suspend fun createConnection(started: Long) {
        return db.connectionDao().createConnection(started)
    }

    suspend fun getLastConnection(): ConnectionEntity? {
        return db.connectionDao().getLastConnection()
    }

    suspend fun updateConnectionLost(id: Int) {
        db.connectionDao().updateConnectionLost(id)
    }

    suspend fun clear() {
        db.connectionDao().clear()
    }

    suspend fun markAsChecked(id: Int) {
        db.connectionDao().markAsChecked(id)
    }
}*/