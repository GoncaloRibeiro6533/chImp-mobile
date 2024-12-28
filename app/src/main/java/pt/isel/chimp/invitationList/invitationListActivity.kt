package pt.isel.chimp.invitationList

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo

class InvitationListActivity: ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val repo by lazy { (application as DependenciesContainer).repo }
    private val userInfoRepo by lazy { (application as DependenciesContainer).userInfoRepository }


    private val viewModel by viewModels<InvitationListViewModel>(
        factoryProducer = {
            InvitationListViewModelFactory(
                chImpService,
                repo,
                userInfoRepo
            )
        }
    )

    private fun onNavigationBack() {
        navigateTo(
            this,
            MenuActivity::class.java
        )
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadLocalData()
        setContent{
            InvitationScreen(
                viewModel = viewModel,
                onNavigationBack = { onNavigationBack() },
                onAccept = {},
                onDecline = {}
            )
        }
    }
}