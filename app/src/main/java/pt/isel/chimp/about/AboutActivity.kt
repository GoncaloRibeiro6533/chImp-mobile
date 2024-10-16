package pt.isel.chimp.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isel.chimp.R
import pt.isel.chimp.TAG


/**
 * The activity responsible for displaying the about screen. Notice the absence of a ViewModel.
 * In this case ee don't need one because the screen is static and doesn't have any dynamic data.
 */
class AboutActivity : ComponentActivity() {
    private val viewModel by viewModels<AboutScreenViewModel>()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "AboutActivity.onCreate() called")
        enableEdgeToEdge()
        setContent {
            AboutScreen(
                onNavigateBack = { finish() },
                onSendEmailRequested = { openSendEmail(it) },
                onOpenUrlRequested = { openURL(it) }
            )
        }
    }

    private fun openSendEmail(email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT)
            }

            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    private fun openURL(url: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to open URL", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}

val defaultAuthors = listOf(
    CreatorInfo(
        name = "Tiago Silva",
        imageId = R.drawable.ic_user_img,
        socials = socialsDefault("tiago15ts"),
        email = "A48252@alunos.isel.pt"
    ),
    CreatorInfo(
        name = "Name",
        imageId = R.drawable.ic_user_img,
        socials = socialsDefault("name"),
        email = "A48xxx@alunos.isel.pt"
    ),
    CreatorInfo(
        name = "Name",
        imageId = R.drawable.ic_user_img,
        socials = socialsDefault("name"),
        email = "A48xxx@alunos.isel.pt"
    )
)


private const val EMAIL_SUBJECT = "About the Channel and Messages App"