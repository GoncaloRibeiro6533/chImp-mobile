package pt.isel.chimp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.repository.ChImpRepo

class SseWorkItem (client: HttpClient, repo: ChImpRepo, context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}


class CoroutineSseWorkItem(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val client = (applicationContext as DependenciesContainer).client
        val repo = (applicationContext as DependenciesContainer).repo
        return withContext(Dispatchers.IO) {
           try {
                client.sse("${ChImpApplication.NGROK}/api/sse/listen") {
                    while (true) {
                        incoming.collect { event ->
                            eventHandler(event, repo)
                        }
                    }
                }
                Result.success()
            } catch (e: Exception) {
                println("Error: $e")
                Result.failure()
            }
        }
    }

    private suspend fun eventHandler(event: ServerSentEvent, repo: ChImpRepo) {
        val eventC = event.event ?: throw Exception("Event is null") //TODO: improve this
        val data = event.data ?: throw Exception("Data is null") //TODO: improve this
        when (eventC) {
            "NewChannelMessage" -> {
                repo.messageRepo.insertMessage(listOf(Json.decodeFromString<Message>(data)))
            }
            "ChannelNameUpdate" -> TODO()
            "ChannelNewMemberUpdate" -> TODO()
            "ChannelMemberExitedUpdate" -> TODO()
            "NewInvitationUpdate" -> TODO()
            "InvitationAcceptedUpdate" -> TODO()
            else -> {
                println("Unknown event: ${event.event}")
            }
        }
    }

   /* private fun parseMessage(data: String): Message {
        val parts =

        return Message(
            id = parts[0].toInt(),
            sender = parts[1].toInt(),
            channel = parts[2].toInt(),
            content = parts[3],
            timestamp = parts[4].toLong()
        )
    }*/
}



