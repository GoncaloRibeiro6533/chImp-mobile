package pt.isel.chimp.repository

import pt.isel.chimp.storage.ChImpClientDB

class ChImpRepoImp(
    db: ChImpClientDB
): ChImpRepo {
    override val channelRepo = ChannelRepository(db)
    override val userRepo = UserRepository(db)
    override val messageRepo = MessageRepository(db)
    override val invitationRepo = InvitationRepository(db)
}