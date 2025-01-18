package pt.isel.chimp.repository

import pt.isel.chimp.repository.interfaces.ChannelRepositoryInterface
import pt.isel.chimp.repository.interfaces.InvitationRepositoryInterface
import pt.isel.chimp.repository.interfaces.MessageRepositoryInterface
import pt.isel.chimp.repository.interfaces.UserRepositoryInterface

interface ChImpRepo {
    val channelRepo: ChannelRepositoryInterface
    val userRepo: UserRepositoryInterface
    val messageRepo: MessageRepositoryInterface
    val invitationRepo: InvitationRepositoryInterface
}