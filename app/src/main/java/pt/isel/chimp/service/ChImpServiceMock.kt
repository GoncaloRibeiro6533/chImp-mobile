package pt.isel.chimp.service

import pt.isel.chimp.service.repo.RepoMockImpl

class ChImpServiceMock : ChImpService {

    override val userService: UserService by lazy {
        MockUserService(RepoMockImpl())
    }
    override val channelService: ChannelService by lazy {
        MockChannelService(RepoMockImpl())
    }
    override val messageService: MessageService by lazy {
        MockMessageService(RepoMockImpl())
    }
}