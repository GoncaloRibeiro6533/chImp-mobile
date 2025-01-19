package pt.isel.chimp.channel.channelList

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import pt.isel.chimp.channels.channelsList.CHANNEL_LIST_TEST_TAG
import pt.isel.chimp.channels.channelsList.ChannelsListScreen
import pt.isel.chimp.channels.channelsList.ChannelsListScreenState
import pt.isel.chimp.channels.channelsList.ChannelsListViewModel
import pt.isel.chimp.components.LOADING_VIEW_TAG
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.profile.ERROR_ALERT
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.repository.interfaces.ChannelRepositoryInterface
import pt.isel.chimp.repository.interfaces.InvitationRepositoryInterface
import pt.isel.chimp.repository.interfaces.MessageRepositoryInterface
import pt.isel.chimp.repository.interfaces.UserRepositoryInterface
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.success

class ChannelListScreenTests {


    @get:Rule
    val composeRule = createComposeRule()

    private val testUserInfo = User(1, "test", "test@example.com")
    private val fakeRepo = object : UserInfoRepository {
        override val userInfo: Flow<User?>
            get() = flow { emit(null) }
        override suspend fun getUserInfo(): User? { delay(1000); return testUserInfo }
        override suspend fun updateUserInfo(userInfo: User) { }
        override suspend fun clearUserInfo() { }
    }

    private val creator = User(1, "test", "test@example.com")
    private val fakeService = object : ChannelService {
        override suspend fun createChannel(
            name: String,
            creatorId: Int,
            visibility: Visibility
        ): Either<ApiError, Channel> = success(Channel(1, "name", creator, visibility))
        override suspend fun getChannelById(id: Int) = success(Channel(1, "name", creator, Visibility.PUBLIC))
        override suspend fun getChannelsOfUser(
            userId: Int,
            limit: Int,
            skip: Int
        ): Either<ApiError, Map<Channel, Role>> = success(mapOf(Channel(1, "name", creator, Visibility.PUBLIC) to Role.READ_WRITE))
        override suspend fun joinChannel(
            userToAdd: Int,
            channelId: Int,
            role: Role
        ): Either<ApiError, Channel> = success(Channel(1, "name", creator, Visibility.PUBLIC))

        override suspend fun getChannelMembers(channelId: Int): Either<ApiError, List<Pair<User, Role>>> =
            success(listOf(Pair(creator, Role.READ_WRITE)))

        override suspend fun updateChannelName(
            channelId: Int,
            newName: String
        ): Either<ApiError, Channel> = success(Channel(1, "name", creator, Visibility.PUBLIC))

        override suspend fun removeUserFromChannel(
            channelId: Int,
            userID: Int
        ): Either<ApiError, Channel> = success(Channel(1, "name", creator, Visibility.PUBLIC))
        override suspend fun searchChannelByName(
            name: String,
            limit: Int,
            skip: Int
        ): Either<ApiError, List<Channel>> = success(listOf(Channel(1, "name", creator, Visibility.PUBLIC)))

    }


    private val fakeChannelRepository = object : ChannelRepositoryInterface {
        override fun getChannels(): Flow<Map<Channel, Role>> = flow { }
        override suspend fun insertChannels(userId: Int, channels: Map<Channel, Role>) {
            delay(10000)
        }
        override suspend fun updateChannel(channel: Channel) { }
        override suspend fun insertUserInChannel(
            userId: Int,
            channelId: Int,
            role: Role
        ) { }
        override suspend fun removeUserFromChannel(userId: Int, channelId: Int) {}
        override suspend fun clear() {}
        override fun getChannelMembers(channel: Channel): Flow<Map<User, Role>>  = flow { }
        override fun getAllChannels(): Flow<List<Channel>> = flow { }
        override suspend fun markChannelAsLoaded(channelId: Int) { }
        override suspend fun isLoaded(channelId: Int): Boolean = false
    }

    private val fakeMessageRepository = object : MessageRepositoryInterface {
        override fun getMessages(channel: Channel): Flow<List<Message>> = flow { }
        override suspend fun insertMessage(messages: List<Message>) { }
        override suspend fun channelHasMessages(channel: Channel): Boolean = false
        override suspend fun deleteMessages(channel: Channel) { }
        override suspend fun clear() { }
    }

    private val fakeInvitationRepository = object : InvitationRepositoryInterface {
        override fun getInvitations(receiver: User): Flow<List<Invitation>> = flow { }
        override suspend fun insertInvitations(invitations: List<Invitation>) {}
        override suspend fun deleteInvitation(invitationId: Int) {}
        override suspend fun deleteAllInvitations() {}
    }

    private val fakeUserRepository = object : UserRepositoryInterface {
        override suspend fun insertUser(users: List<User>) { }
        override fun getUsers(): Flow<List<User>> = flow { }
        override suspend fun updateUser(user: User) { }
        override suspend fun clear() { }
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
    private fun createFakeViewModel(initialState: ChannelsListScreenState): ChannelsListViewModel =
        ChannelsListViewModel(
            userInfo = fakeRepo,
            channelService = fakeService,
            repo = fakeRepository,
            initialState = initialState
        )

    @Test
    fun when_channel_List_is_unitialized_then_no_channels_are_shown() {
        val viewModel = createFakeViewModel(ChannelsListScreenState.Uninitialized)
        composeRule.setContent {
            ChannelsListScreen(viewModel)
        }
        composeRule.onNodeWithTag(LOADING_VIEW_TAG).assertDoesNotExist()
    }

    @Test
    fun when_channel_List_is_loading_then_loading_is_displayed() {
        val viewModel = createFakeViewModel(ChannelsListScreenState.LoadFromRemote)
        composeRule.setContent {
            ChannelsListScreen(viewModel)
        }
        composeRule.onNodeWithTag(LOADING_VIEW_TAG).assertIsDisplayed()
    }

    @Test
    fun when_channel_List_is_success_then_channels_are_shown() {
        val viewModel = createFakeViewModel(ChannelsListScreenState.Success(
            MutableStateFlow(
                mapOf(
                    Channel(1, "name", creator, Visibility.PUBLIC)
                            to Role.READ_WRITE))
        ))
        composeRule.setContent {
            ChannelsListScreen(viewModel)
        }
        composeRule.onNodeWithTag(LOADING_VIEW_TAG).assertDoesNotExist()
        composeRule.onNodeWithTag(CHANNEL_LIST_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun when_state_is_saveData_LoadingView_is_shown() {
        val viewModel = createFakeViewModel(ChannelsListScreenState.SaveData(testUserInfo, mapOf(
            Channel(1, "name", creator, Visibility.PUBLIC) to Role.READ_WRITE
        )))
        composeRule.setContent {
            ChannelsListScreen(viewModel)
        }
        composeRule.onNodeWithTag(LOADING_VIEW_TAG).assertIsDisplayed()
    }


    @Test
    fun when_state_is_error_ErrorAlert_is_shown() {
        val viewModel = createFakeViewModel(ChannelsListScreenState.Error(ApiError("Error fetching user")))
        composeRule.setContent {
            ChannelsListScreen(viewModel)
        }
        composeRule.onNodeWithTag(ERROR_ALERT).assertIsDisplayed()
    }
}