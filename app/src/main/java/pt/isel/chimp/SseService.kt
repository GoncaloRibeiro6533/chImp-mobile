package pt.isel.chimp
/*
import android.R
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import io.ktor.client.plugins.sse.sse
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import pt.isel.chimp.workManager.sse.ChannelMemberExitedUpdate
import pt.isel.chimp.workManager.sse.ChannelNameUpdate
import pt.isel.chimp.workManager.sse.ChannelNewMemberUpdate
import pt.isel.chimp.workManager.sse.InvitationAcceptedUpdate
import pt.isel.chimp.workManager.sse.NewChannelMessage
import pt.isel.chimp.workManager.sse.NewInvitationUpdate
import pt.isel.chimp.workManager.sse.NewMember
import pt.isel.chimp.workManager.sse.RemovedMember
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.message.Message

class SseForegroundService : Service() {


    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SSEService", "Service started")
        startForeground(1, buildNotification("Listening for updates..."))
        listenToSse()
        return START_STICKY // Garante que o serviço será reiniciado se for interrompido
    }

    private fun listenToSse() {
        serviceScope.launch {
            val client = (applicationContext as DependenciesContainer).client
            try {
                client.sse("${ChImpApplication.NGROK}/api/sse/listen") {
                    incoming.collect { event ->
                        handleSseEvent(event)
                    }
                }
            } catch (e: Exception) {
                Log.e("SSEService", "Error listening to SSE: $e")
            }
        }
    }

    private suspend fun handleSseEvent(event: ServerSentEvent) {
        val repo = (applicationContext as DependenciesContainer).repo
        val eventType = event.event ?: return
        val data = event.data ?: return
        Log.d("SSEService", "Event received: $eventType")
        when (eventType) {
            "NewChannelMessage" -> {
                val message = messageMapper(data)
                repo.messageRepo.insertMessage(listOf(message))
            }
            "ChannelNameUpdate" -> {
                val channel = channelNameUpdateMapper(data)
                repo.channelRepo.updateChannel(channel)
            }
            "ChannelNewMemberUpdate" -> {
                val newMember = channelNewMemberUpdateMapper(data)
                repo.userRepo.insertUser(listOf(NewMember.newMember))
                repo.channelRepo.insertUserInChannel(
                    NewMember.newMember.id, NewMember.channel.id,
                    NewMember.role
                )
            }
            "ChannelMemberExitedUpdate" -> {
                val removedMember = channelMemberExitedUpdateMapper(data)
                repo.channelRepo.removeUserFromChannel(RemovedMember.removedMember.id, RemovedMember.channel.id)
            }
            "NewInvitationUpdate" -> {
                val invitation = invitationMapper(data)
               // repo.invitationRepo.insertInvitations(listOf(invitation))
            }
            "InvitationAcceptedUpdate" -> {
                val invitation = invitationAcceptedMapper(data)
               // repo.invitationRepo.deleteInvitation(invitation)
            }
            else -> {
                Log.d("SSEService", "Unhandled event: $eventType")
            }
        }
    }


    private fun sendNotification(title: String, content: String) {
        val notification = NotificationCompat.Builder(this, "SSE_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationId = System.currentTimeMillis().toInt() // ID único para cada notificação
        startForeground(notificationId, notification) // Atualiza a notificação do serviço
    }

    private fun buildNotification(content: String): Notification {
        return NotificationCompat.Builder(this, "SSE_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle("SSE Service")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel() // Cancela todos os jobs em execução
        Log.d("SSEService", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun messageMapper(data: String): Message {
        val eventData = Json.decodeFromString<NewChannelMessage>(data)
        return eventData.message
    }

    private fun channelNameUpdateMapper(data: String): Channel {
        val eventData = Json.decodeFromString<ChannelNameUpdate>(data)
        return eventData.channel
    }

    private fun channelNewMemberUpdateMapper(data: String): NewMember {
        val eventData = Json.decodeFromString<ChannelNewMemberUpdate>(data)
        return eventData.newMember
    }

    private fun channelMemberExitedUpdateMapper(data: String): RemovedMember {
        val eventData = Json.decodeFromString<ChannelMemberExitedUpdate>(data)
        return eventData.removedMember
    }

    private fun invitationMapper(data: String): Invitation {
        val eventData = Json.decodeFromString<NewInvitationUpdate>(data)
        return eventData.invitation
    }

    private fun invitationAcceptedMapper(data: String): Invitation {
        val eventData = Json.decodeFromString<InvitationAcceptedUpdate>(data)
        return eventData.invitation
    }
}*/