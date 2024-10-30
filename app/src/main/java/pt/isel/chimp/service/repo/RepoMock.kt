package pt.isel.chimp.service.repo

interface RepoMock {
    val userRepoMock: UserRepoMock
    val channelRepoMock: ChannelRepoMock
    val messageRepoMock: MessageRepoMock
    //val invitatioRepoMock : InvitationRepoMock TODO
}

class RepoMockImpl : RepoMock {
    override val userRepoMock: UserRepoMock = UserRepoMock()
    override val channelRepoMock: ChannelRepoMock = ChannelRepoMock()
    override val messageRepoMock: MessageRepoMock = MessageRepoMock()
}
