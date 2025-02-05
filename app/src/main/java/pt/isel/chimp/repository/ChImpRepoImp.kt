package pt.isel.chimp.repository

import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.storage.ChImpClientDB

class ChImpRepoImp(
    local: ChImpClientDB,
    remote: ChImpService
): ChImpRepo {
    override val channelRepo = ChannelRepository(local, remote)
    override val userRepo = UserRepository(local, remote)
    override val messageRepo = MessageRepository(local, remote)
    override val invitationRepo = InvitationRepository(local, remote)
}