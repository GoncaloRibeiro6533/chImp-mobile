package pt.isel.chimp.invitationList

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.domain.invitation.Invitation

class InvitationListActivity: ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val repo by lazy { (application as DependenciesContainer).repo }


    private val viewModel by viewModels<InvitationListViewModel>(
        factoryProducer = {
            InvitationListViewModelFactory(
                chImpService,
                repo
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            InvitationScreen(
                viewModel = viewModel,
                onNavigationBack = { finish() },
                onAccept = {},
                onDecline = {}
            )
        }
    }
}