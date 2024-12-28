package pt.isel.chimp.channels.createInvitation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.ChannelParcelable
import kotlin.getValue


class CreateInvitationActivity: ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val viewModel by viewModels<CreateInvitationViewModel>(
        factoryProducer = {
            CreateInvitationViewModelFactory(
                userInfoRepository,
                chImpService
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
