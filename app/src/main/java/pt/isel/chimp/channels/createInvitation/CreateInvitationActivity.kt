package pt.isel.chimp.channels.createInvitation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.domain.ChannelParcelable
import kotlin.getValue


class CreateInvitationActivity: ComponentActivity() {

    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val repo by lazy { (application as DependenciesContainer).repo }
    private val viewModel by viewModels<CreateInvitationViewModel>(
        factoryProducer = {
            CreateInvitationViewModelFactory(
                userInfoRepository,
                repo
            )
        }
    )


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!intent.hasExtra("channel")) {
            finish()
            return
        }
        val channel = intent.getParcelableExtra("channel", ChannelParcelable::class.java)
        if (channel == null) {
            finish()
            return
        }
        setContent{
            CreateInvitationScreen(
                viewModel = viewModel,
                channel =  channel.toChannel(),
                onNavigationBack = { finish() }
            )
        }
    }
}
