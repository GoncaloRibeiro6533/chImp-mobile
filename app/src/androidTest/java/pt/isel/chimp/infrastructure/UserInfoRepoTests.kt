package pt.isel.chimp.infrastructure

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.utils.CleanDataStoreRule

@RunWith(AndroidJUnit4::class)
class UserInfoRepoTests {

    @get:Rule
    val cleanDataStoreRule = CleanDataStoreRule()

    @Test
    fun getUserInfo_returns_null_when_no_user_is_stored() = runTest {
        val sut = UserInfoRepo(cleanDataStoreRule.dataStore)
        val user = sut.getUserInfo()
        assert(user == null)
    }

    @Test
    fun updateUserInfo_stores_user_correctly() = runTest {
        val sut = UserInfoRepo(cleanDataStoreRule.dataStore)
        val expectedUser = User(id = 1, username = "TestUser", email = "test@example.com")
        sut.updateUserInfo(expectedUser)

        val actualUser = sut.getUserInfo()
        assert(actualUser == expectedUser)
    }

    @Test
    fun userInfoFlow_emits_null_when_no_user_is_stored() = runTest {
        val sut = UserInfoRepo(cleanDataStoreRule.dataStore)
        val user = sut.userInfo.first()
        assert(user == null)
    }

    @Test
    fun userInfoFlow_emits_user_after_user_is_stored() = runTest {
        val sut = UserInfoRepo(cleanDataStoreRule.dataStore)
        val expectedUser = User(id = 2, username = "AnotherUser", email = "another@example.com")
        sut.updateUserInfo(expectedUser)

        val user = sut.userInfo.first()
        assert(user == expectedUser)
    }

    @Test
    fun clearUserInfo_removes_stored_user() = runTest {
        val sut = UserInfoRepo(cleanDataStoreRule.dataStore)
        val user = User(id = 3, username = "ToDelete", email = "delete@example.com")
        sut.updateUserInfo(user)

        sut.clearUserInfo()
        val clearedUser = sut.getUserInfo()
        assert(clearedUser == null)
    }

    @Test
    fun storing_and_clearing_user_does_not_affect_flow() = runTest {
        val sut = UserInfoRepo(cleanDataStoreRule.dataStore)
        val user = User(id = 4, username = "FlowUser", email = "flow@example.com")
        sut.updateUserInfo(user)

        val userInFlow = sut.userInfo.first()
        assert(userInFlow == user)

        sut.clearUserInfo()
        val clearedUser = sut.userInfo.first()
        assert(clearedUser == null)
    }
}
