package pt.isel.chimp.channels.channelsList

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import pt.isel.chimp.CoroutineSseWorkItem
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.channel.ChannelActivity
import pt.isel.chimp.channels.createChannel.CreateChannelActivity
import pt.isel.chimp.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo

class ChannelsListActivity : ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val repo by lazy { (application as DependenciesContainer).repo }

    private val viewModel by viewModels<ChannelsListViewModel>(
        factoryProducer = {
            ChannelsListViewModelFactory(
                userInfoRepository,
                chImpService,
                repo
            )
        }
    )

    private fun navigateToChannel(channel: ChannelParcelable){
        val intent = Intent(this, ChannelActivity::class.java).putExtra("channel", channel)
        this.startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<CoroutineSseWorkItem>()
                .setConstraints(constraints)
                .addTag("sse")
                .build()
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                "SseWork",
                ExistingWorkPolicy.KEEP,
                workRequest)
        /* val intent = Intent(this, SseForegroundService::class.java)
         startService(intent)*/
        viewModel.loadLocalData()
        setContent {
            ChannelsListScreen(
                viewModel = viewModel,
                onMenuRequested = {
                    navigateTo(this, MenuActivity::class.java)
                    finish()
                },
                onChannelSelected = { channel ->
                    navigateToChannel(channel)
                },
                onNavigateToCreateChannel = { navigateTo(this, CreateChannelActivity::class.java) },
                onFatalError = {
                    viewModel.onFatalError()
                    WorkManager.getInstance(applicationContext).cancelAllWork()
                    finish()
                }
            )
        }

        //TODO maybe disable
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateTo(this@ChannelsListActivity, MenuActivity::class.java)
            }
        })

    }

}

