package pt.isel.chimp.service

class ChImpServiceMock : ChImpService {

    override val userService: UserService by lazy {
        MockUserService()
    }
    override val channelService: ChannelService by lazy {
        MockChannelService()
    }
}