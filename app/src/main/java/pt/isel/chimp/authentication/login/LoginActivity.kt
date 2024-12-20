package pt.isel.chimp.authentication.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.authentication.register.RegisterActivity
import pt.isel.chimp.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.utils.navigateTo

class LoginActivity: ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val repository by lazy { (application as DependenciesContainer).repo }
    private val db by lazy { (application as DependenciesContainer).clientDB }

    private val viewModel by viewModels<LoginScreenViewModel>(
        factoryProducer = {
            LoginScreenViewModelFactory(
                userInfoRepository,
                repository,
                chImpService
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccessful = {
                        navigateTo(this,ChannelsListActivity::class.java)
                        finish()
                },
                onBackRequested = { finish() },
                onRegisterRequested = {
                    navigateTo(this, RegisterActivity::class.java)
                    finish()
                }
            )
        }

    }
}