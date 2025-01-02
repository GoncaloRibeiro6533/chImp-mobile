package pt.isel.chimp.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.repository.UserInfoRepository

class AboutScreenViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                val userInfo = userInfoRepository.getUserInfo()
                // Handle the userInfo as needed
            } catch (e: Exception) {
                // Handle the exception as needed
            }
        }
    }


}

@Suppress("UNCHECKED_CAST")
class AboutScreenViewModelFactory(
    private val userInfoRepository: UserInfoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AboutScreenViewModel(
            userInfoRepository
        ) as T
    }
}
