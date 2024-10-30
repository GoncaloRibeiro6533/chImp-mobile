package pt.isel.chimp.service

interface ChImpService {
    val userService: UserService
    val channelService: ChannelService
    val messageService : MessageService
}