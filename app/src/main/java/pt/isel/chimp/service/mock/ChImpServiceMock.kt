package pt.isel.chimp.service.mock

import pt.isel.chimp.infrastructure.CookiesRepo
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.service.repo.RepoMockImpl

class ChImpServiceMock(
    private val cookieStorage: CookiesRepo,
    private val repo: ChImpRepo
) : ChImpService {

    val repoMock : RepoMockImpl by lazy {
        RepoMockImpl(cookieStorage)
    }

    override val userService: UserService by lazy {
        MockUserService(repoMock, cookieStorage)
    }
    override val channelService: ChannelService by lazy {
        MockChannelService(repoMock, cookieStorage)
    }
    override val messageService: MessageService by lazy {
        MockMessageService(repoMock, cookieStorage, repo)
    }

    override val invitationService: InvitationService by lazy {
        MockInvitationService(repoMock, cookieStorage)
    }
}