package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.service.UserService

class ChImpServiceHttp(private val client: HttpClient): ChImpService {

    override val userService: UserService by lazy {
        UserServiceHttp(client)
    }

    override val channelService: ChannelService by lazy {
        TODO()
    }

    override val messageService: MessageService by lazy {
        TODO()
    }
}