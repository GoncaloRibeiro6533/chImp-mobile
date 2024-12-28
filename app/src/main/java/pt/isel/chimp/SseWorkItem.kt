package pt.isel.chimp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.repository.ChImpRepo

class SseWorkItem (client: HttpClient, repo: ChImpRepo, context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}
//https://developer.android.com/develop/background-work/background-tasks/persistent/how-to/long-running?hl=pt-br


class CoroutineSseWorkItem(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SSE Service"
            val descriptionText = "Service to listen for SSE events"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("SSE_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override suspend fun doWork(): Result {
        val client = (applicationContext as DependenciesContainer).client
        val repo = (applicationContext as DependenciesContainer).repo
        val userInfo = (applicationContext as DependenciesContainer).userInfoRepository
        setForeground(createForegroundInfo("Listening for events"))
        return withContext(Dispatchers.IO) {
           // while(!isStopped) {
                try {
                    client.sse("${ChImpApplication.NGROK}/api/sse/listen") {
                        incoming.collect { event ->
                            eventHandler(event, repo, userInfo)
                        }
                        delay(5000)
                    }
                } catch (e: Exception) {
                    println("Error: $e")
                    return@withContext Result.retry()
                }
            //}
            Result.success()
        }

    }

    private fun createForegroundInfo(notification: String): ForegroundInfo {
        val id = ("SSE_CHANNEL_ID")
        val title = ("Chimp")
        val anchor = ("See")
        // This PendingIntent can be used to cancel the worker
        // val intent = Intent

        // Create a Notification channel if necessary
        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(notification)
            .setSmallIcon(R.drawable.logo_icon)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            //.addAction(android.R.drawable.ic_delete, anchor, intent)
            .build()

        return ForegroundInfo(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }

    private fun sendNotification(title: String, content: String) {
        val notificationId = System.currentTimeMillis().toInt() // Usando o timestamp como ID único para cada notificação

        // Criando a notificação
        val notification = NotificationCompat.Builder(applicationContext, "SSE_CHANNEL_ID")
            .setSmallIcon(R.drawable.logo_icon) // Ícone da notificação
            .setContentTitle(title) // Título da notificação
            .setContentText(content) // Conteúdo da notificação
            .setVibrate(longArrayOf(500, 500)) // Vibração
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridade alta para visibilidade imediata
            .build()

        // Enviar a notificação usando NotificationManager
        notificationManager.notify(notificationId, notification) // Usar ID único para cada notificação
    }


   /* private fun createChannel() {
            val channel = NotificationChannel(
                "SSE_CHANNEL_ID", "SSE Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                this.description = description
            }
            val notificationManager = (applicationContext).getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
    }*/


    private suspend fun eventHandler(event: ServerSentEvent, repo: ChImpRepo, userInfo: UserInfoRepository) {
        val eventC = event.event ?: throw Exception("Event is null") //TODO: improve this
        val data = event.data ?: throw Exception("Data is null") //TODO: improve this
        val user = userInfo.getUserInfo()
        when (eventC) {
            "NewChannelMessage" -> {
                val message = messageMapper(data)
                   if (repo.messageRepo.channelHasMessages(message.channel)) {
                        repo.messageRepo.insertMessage(listOf(message))
                    }
                    val notificationTitle = "New Message"
                    val notificationContent = "New message from:${message.sender.username} at channel:${message.channel.name}"
                    if (message.sender != user ) sendNotification(notificationTitle, notificationContent)
                }
            "ChannelNameUpdate" -> {
                    val channel = channelNameUpdateMapper(data)
                    repo.channelRepo.updateChannel(channel)
            }
            "ChannelNewMemberUpdate" -> {
                val newMember = channelNewMemberUpdateMapper(data)
                repo.userRepo.insertUser(listOf(newMember.newMember))
                repo.channelRepo.insertUserInChannel(newMember.newMember.id, newMember.channel.id, newMember.role)
            }
            "ChannelMemberExitedUpdate" -> {
                val removedMember = channelMemberExitedUpdateMapper(data)
                repo.channelRepo.removeUserFromChannel(removedMember.removedMember.id, removedMember.channel.id)
            }
            "NewInvitationUpdate" -> {
                val invitation = invitationMapper(data)
                repo.invitationRepo.insertInvitations(listOf(invitation))
            }
            "InvitationAcceptedUpdate" -> {
                val invitation = invitationAcceptedMapper(data)
                repo.invitationRepo.deleteInvitation(invitation.id)
            }
            else -> {
                println("Unknown event: ${event.event}")
            }
        }
    }

    private fun messageMapper(data: String): Message {
        val eventData = Json.decodeFromString<NewChannelMessage>(data)
        return eventData.message
    }

    private fun channelNameUpdateMapper(data: String): Channel {
        val eventData = Json.decodeFromString<Channel>(data)
        return eventData
    }

    private fun channelNewMemberUpdateMapper(data: String): NewMember {
        val eventData = Json.decodeFromString<ChannelNewMemberUpdate>(data)
        return eventData.newMember
    }

    private fun channelMemberExitedUpdateMapper(data: String): RemovedMember {
        val eventData = Json.decodeFromString<RemovedMember>(data)
        return eventData
    }

    private fun invitationMapper(data: String): Invitation {
        val eventData = Json.decodeFromString<Invitation>(data)
        return eventData
    }

    private fun invitationAcceptedMapper(data: String): Invitation {
        val eventData = Json.decodeFromString<Invitation>(data)
        return eventData
    }

}


@Serializable
data class NewChannelMessage(
    val id: Long, // SSE Event identifier
    val message: Message, // New channel message
)

@Serializable
data class ChannelNameUpdate(
    val id: Long, // SSE Event identifier
    val channel: Channel, // New channel
)

@Serializable
data class ChannelNewMemberUpdate(
    val id: Long, // SSE Event identifier
    val newMember: NewMember,
)

@Serializable
data class ChannelMemberExitedUpdate(
    val id: Long, // SSE Event identifier
    val removedMember: RemovedMember,
)

@Serializable
data class NewInvitationUpdate(
    val id: Long, // SSE Event identifier
    val invitation: Invitation, // New invitation
)
@Serializable
data class InvitationAcceptedUpdate(
    val id: Long, // SSE Event identifier
    val invitation: Invitation, // Accepted invitation
)

@Serializable
data class NewMember(
    val channel: Channel, // Channel with updated members
    val newMember: User,
    val role : Role
)

@Serializable
data class RemovedMember(
    val channel: Channel, // Channel with updated members
    val removedMember: User
)


