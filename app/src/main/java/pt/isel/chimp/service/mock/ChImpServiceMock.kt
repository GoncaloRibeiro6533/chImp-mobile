package pt.isel.chimp.service.mock

import io.ktor.client.plugins.cookies.CookiesStorage
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.service.repo.RepoMockImpl

class ChImpServiceMock(
    private val cookieStorage: CookiesStorage
) : ChImpService {
    //TODO
    val repoCookie : CookieRepoMock by lazy {
        CookieRepoMock()
    }


    val repoMock : RepoMockImpl by lazy {
        RepoMockImpl()
    }

    override val userService: UserService by lazy {
        MockUserService(repoMock, cookieStorage)
    }
    override val channelService: ChannelService by lazy {
        MockChannelService(repoMock, cookieStorage)
    }
    override val messageService: MessageService by lazy {
        MockMessageService(repoMock, cookieStorage)
    }

    override val invitationService: InvitationService by lazy {
        MockInvitationService(repoMock, cookieStorage)
    }
}