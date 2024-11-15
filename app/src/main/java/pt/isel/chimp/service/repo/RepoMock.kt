package pt.isel.chimp.service.repo

interface RepoMock {
    val userRepoMock: UserRepoMock
    val channelRepoMock: ChannelRepoMock
    val messageRepoMock: MessageRepoMock
    val invitationRepoMock : InvitationRepoMock
}

class RepoMockImpl : RepoMock {
    override val userRepoMock: UserRepoMock = UserRepoMock()
    override val channelRepoMock: ChannelRepoMock = ChannelRepoMock()
    override val messageRepoMock: MessageRepoMock = MessageRepoMock()
    override val invitationRepoMock: InvitationRepoMock = InvitationRepoMock()
}
