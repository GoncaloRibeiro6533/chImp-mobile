package pt.isel.chimp.service.mock

import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.service.repo.RepoMockImpl

class ChImpServiceMock : ChImpService {

    val repoMock : RepoMockImpl by lazy {
        RepoMockImpl()
    }

    override val userService: UserService by lazy {
        MockUserService(repoMock)
    }
    override val channelService: ChannelService by lazy {
        MockChannelService(repoMock)
    }
    override val messageService: MessageService by lazy {
        MockMessageService(repoMock)
    }

    override val invitationService: InvitationService by lazy {
        MockInvitationService(repoMock)
    }
}