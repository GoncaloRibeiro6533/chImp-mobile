package pt.isel.chimp.service.repo

import pt.isel.chimp.infrastructure.CookiesRepo

interface RepoMock {
    val userRepoMock: UserRepoMock
    val channelRepoMock: ChannelRepoMock
    val messageRepoMock: MessageRepoMock
    val invitationRepoMock : InvitationRepoMock
}

class RepoMockImpl(cookiesStorage: CookiesRepo) : RepoMock {
    override val userRepoMock: UserRepoMock = UserRepoMock(cookiesStorage)
    override val channelRepoMock: ChannelRepoMock = ChannelRepoMock()
    override val messageRepoMock: MessageRepoMock = MessageRepoMock()
    override val invitationRepoMock: InvitationRepoMock = InvitationRepoMock()
}
