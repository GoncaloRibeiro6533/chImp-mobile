package pt.isel.chimp.service.repo

interface RepoMock {
    val userRepoMock: UserRepoMock
    //val channelRepoMock: ChannelRepoMock TODO
    //val messageRepoMock: MessageRepoMock TODO
}

class RepoMockImpl : RepoMock {
    override val userRepoMock: UserRepoMock = UserRepoMock()
    //override val channelRepoMock: ChannelRepoMock = ChannelRepoMock TODO
    //override val messageRepoMock: MessageRepoMock = MessageRepoMock TODO
}
