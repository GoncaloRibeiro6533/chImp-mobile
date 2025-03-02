package pt.isel.chimp.profile

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.repository.interfaces.ChannelRepositoryInterface
import pt.isel.chimp.repository.interfaces.InvitationRepositoryInterface
import pt.isel.chimp.repository.interfaces.MessageRepositoryInterface
import pt.isel.chimp.repository.interfaces.UserRepositoryInterface
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.ReplaceMainDispatcherRule
import pt.isel.chimp.utils.success

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTests {

    @get:Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val testUserInfo = User(1, "test", "test@example.com")
    private val fakeRepo = object : UserInfoRepository {
        var fail = false
        override val userInfo: Flow<User?>
            get() = flow { emit(null) }
        override suspend fun getUserInfo(): User? {
            delay(10000)
            if (fail) return null
            return testUserInfo }
        override suspend fun updateUserInfo(userInfo: User) { }
        override suspend fun clearUserInfo() { }
    }

    private val fakeChannelRepository = object : ChannelRepositoryInterface {
        override suspend fun fetchChannels(
            user: User,
            limit: Int,
            skip: Int
        ): Flow<Map<Channel, Role>> {
            TODO("Not yet implemented")
        }

        override suspend fun insertChannels(userId: Int, channels: Map<Channel, Role>) { }
        override suspend fun updateChannel(channel: Channel) { }
        override suspend fun insertUserInChannel(
            userId: Int,
            channelId: Int,
            role: Role
        ) { }
        override suspend fun removeUserFromChannel(userId: Int, channelId: Int) {}
        override suspend fun clear() {}
        override suspend fun findByName(
            name: String,
            limit: Int,
            skip: Int
        ): Either<ApiError, List<Channel>> {
            TODO("Not yet implemented")
        }

        override suspend fun markChannelAsLoaded(channelId: Int) { }
        override suspend fun isLoaded(channelId: Int): Boolean = false
        override suspend fun createChannel(
            name: String,
            creatorId: Int,
            visibility: Visibility
        ): Either<ApiError, Channel> {
            TODO("Not yet implemented")
        }

        override suspend fun changeChannelName(
            channel: Channel,
            name: String
        ): Either<ApiError, Channel> {
            TODO("Not yet implemented")
        }

        override suspend fun leaveChannel(
            channel: Channel,
            user: User
        ): Either<ApiError, Channel> {
            TODO("Not yet implemented")
        }

        override suspend fun fetchChannelMembers(channel: Channel): Flow<Map<User, Role>> {
            TODO("Not yet implemented")
        }

        override fun getChannel(channel: Channel): Flow<Channel> {
            TODO("Not yet implemented")
        }

        override suspend fun joinChannel(
            user: User,
            channel: Channel,
            role: Role
        ): Either<ApiError, Channel> {
            TODO("Not yet implemented")
        }
    }

    private val fakeMessageRepository = object : MessageRepositoryInterface {
        override suspend fun fetchMessages(
            channel: Channel,
            limit: Int,
            skip: Int
        ): Flow<List<Message>> {
            TODO("Not yet implemented")
        }

        override suspend fun insertMessage(messages: List<Message>) { }
        override suspend fun channelHasMessages(channel: Channel): Boolean = false
        override suspend fun deleteMessages(channel: Channel) { }
        override suspend fun clear() { }
    }

    private val fakeInvitationRepository = object : InvitationRepositoryInterface {
        override suspend fun fetchInvitations(receiver: User): Flow<List<Invitation>> {
            TODO("Not yet implemented")
        }

        override suspend fun hasInvitations(): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun insertInvitations(invitations: List<Invitation>) {}
        override suspend fun deleteInvitation(invitationId: Int) {}
        override suspend fun deleteAllInvitations() {}
        override suspend fun acceptInvitation(invitation: Invitation): Either<ApiError, Channel> {
            TODO("Not yet implemented")
        }

        override suspend fun declineInvitation(invitation: Invitation): Either<ApiError, Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun createInvitation(
            receiverId: Int,
            channelId: Int,
            role: Role
        ): Either<ApiError, Unit> {
            TODO("Not yet implemented")
        }
    }

    private val fakeUserRepository = object : UserRepositoryInterface {
        override suspend fun insertUser(users: List<User>) { }
        override suspend fun changeUsername(newUsername: String): Either<ApiError, User> {
            TODO("Not yet implemented")
        }

        override fun getUsers(): Flow<List<User>> = flow { }
        override suspend fun updateUser(user: User) { }
        override suspend fun clear() { }
        override suspend fun fetchByUsername(username: String): Either<ApiError, List<User>> {
            TODO("Not yet implemented")
        }
    }

    private val fakeRepository = object : ChImpRepo {
        override val channelRepo: ChannelRepositoryInterface
            get() = fakeChannelRepository
        override val userRepo: UserRepositoryInterface
            get() = fakeUserRepository
        override val messageRepo: MessageRepositoryInterface
            get() = fakeMessageRepository
        override val invitationRepo: InvitationRepositoryInterface
            get() = fakeInvitationRepository

    }

    @Test
    fun initial_state_is_Idle() {
            val sut = ProfileScreenViewModel(
                fakeRepo,
                fakeRepository
            )
            val state = sut.state.value
            assert(state is ProfileScreenState.Idle)
    }

    @Test
    fun when_fetchProfile_is_called_state_transitions_to_loading() {
        val sut = ProfileScreenViewModel(
            fakeRepo,
            fakeRepository
        )
        sut.fetchProfile()
        val state = sut.state.value
        assert(state is ProfileScreenState.Loading)
    }

    @Test
    fun when_editProfile_is_called_state_is_Editing() {
        val sut = ProfileScreenViewModel(
            fakeRepo,
            fakeRepository,
            initialState = ProfileScreenState.Success(Profile("test", "test@example.com"))
        )
        sut.setEditState(Profile("test", "test@example.com"))
        val state = sut.state.value
        assert(state is ProfileScreenState.EditingUsername)
    }

   /* @Test
    fun when_fetchProfile_is_called_and_error_is_displayed() {
        val sut = ProfileScreenViewModel(
            fakeRepo,
            fakeService,
            fakeRepository
        )
        fakeRepo.fail = true
        sut.fetchProfile()
        fakeRepo.fail = false
        val state = sut.state.value
        assert(state is ProfileScreenState.Error)
    }*/

    @Test
    fun when_saveProfile_is_called_state_transitions_to_loading() {
        val sut = ProfileScreenViewModel(
            fakeRepo,
            fakeRepository,
            initialState = ProfileScreenState.EditingUsername(Profile("test", "test@example.com"))
        )
        sut.editUsername("test")
        val state = sut.state.value
        assert(state is ProfileScreenState.Loading)
    }

    @Test
    fun when_cancel_is_called_state_transitions_to_success() {
        val sut = ProfileScreenViewModel(
            fakeRepo,
            fakeRepository,
            initialState = ProfileScreenState.EditingUsername(Profile("test", "test@example.com"))
        )
        sut.setSuccessState(Profile("test", "test@example.com"))
        val state = sut.state.value
        assert(state is ProfileScreenState.Success)
    }

}