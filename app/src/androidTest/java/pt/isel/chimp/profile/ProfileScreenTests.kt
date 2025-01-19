package pt.isel.chimp.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test
import pt.isel.chimp.components.LOADING_VIEW_TAG
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
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
import pt.isel.chimp.utils.success

class ProfileScreenTests {

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

    private val fakeService = object : UserService {
        override suspend fun fetchUser(): Either<ApiError, User> = success(testUserInfo)
        override suspend fun updateUsername(username: String) = success(testUserInfo)
        override suspend fun login(username: String, password: String) = success(testUserInfo)
        override suspend fun register(username: String, email: String, password: String) = success(testUserInfo)
        override suspend fun findUserById(id: Int): Either<ApiError, User> = success(testUserInfo)
        override suspend fun logout() = success(Unit)
        override suspend fun findUserByUsername(query: String): Either<ApiError, List<User>> =
            success(listOf(testUserInfo))
    }

    private val fakeChannelRepository = object : ChannelRepositoryInterface {
        override fun getChannels(): Flow<Map<Channel, Role>> = flow { }
        override suspend fun insertChannels(userId: Int, channels: Map<Channel, Role>) { }
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
    private fun createFakeViewModel(initialState: ProfileScreenState): ProfileScreenViewModel =
        ProfileScreenViewModel(
            repo = fakeRepo,
            userServices = fakeService,
            db = fakeRepository,
            initialState = initialState
        )


    @Test
    fun when_Idle_fetchProfile_is_called_and_loading_is_displayed() {
        val viewModel = createFakeViewModel(ProfileScreenState.Idle)

        composeRule.setContent {
            ProfileScreen(viewModel = viewModel, onNavigateBack = {})
        }

        composeRule.onNodeWithTag(LOADING_VIEW_TAG).assertIsDisplayed()
    }

    @Test
    fun when_Success_profile_view_is_displayed() {
        val viewModel = createFakeViewModel(
            ProfileScreenState.Success(Profile("testUser", "test@example.com"))
        )

        composeRule.setContent {
            ProfileScreen(viewModel = viewModel, onNavigateBack = {})
        }

        composeRule.onNodeWithTag(PROFILE_VIEW_TAG).assertIsDisplayed()
    }

    @Test
    fun when_EditingUsername_editing_view_is_displayed() {
        val viewModel = createFakeViewModel(
            ProfileScreenState.EditingUsername(Profile("testUser", "test@example.com"))
        )

        composeRule.setContent {
            ProfileScreen(viewModel = viewModel, onNavigateBack = {})
        }

        composeRule.onNodeWithTag(EDIT_USERNAME_VIEW_TAG).assertIsDisplayed()
    }

    @Test
    fun when_Error_error_alert_is_displayed() {
        val viewModel = createFakeViewModel(
            ProfileScreenState.Error(ApiError("Error fetching user"))
        )

        composeRule.setContent {
            ProfileScreen(viewModel = viewModel, onNavigateBack = {})
        }

        composeRule.onNodeWithTag(ERROR_ALERT).assertIsDisplayed()
    }
}
