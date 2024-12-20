package pt.isel.chimp.repository

interface ChImpRepo {
    val channelRepo: ChannelRepository
    val userRepo: UserRepository
    val messageRepo: MessageRepository

}