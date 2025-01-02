package pt.isel.chimp.service.http

import io.ktor.client.HttpClient
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.service.service.http.ChannelServiceHttp
import pt.isel.chimp.service.service.http.InvitationServiceHttp
import pt.isel.chimp.service.service.http.MessageServiceHttp
import pt.isel.chimp.service.service.http.UserServiceHttp

class ChImpServiceHttp(private val client: HttpClient): ChImpService {

    override val userService: UserService by lazy {
        UserServiceHttp(client)
    }

    override val channelService: ChannelService by lazy {
        ChannelServiceHttp(client)
    }

    override val messageService: MessageService by lazy {
        MessageServiceHttp(client)
    }

    override val invitationService: InvitationService by lazy {
        InvitationServiceHttp(client)
    }
}