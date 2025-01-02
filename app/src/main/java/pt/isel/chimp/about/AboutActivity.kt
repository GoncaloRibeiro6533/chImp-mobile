package pt.isel.chimp.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.R


/**
 * The activity responsible for displaying the about screen. Notice the absence of a ViewModel.
 * In this case ee don't need one because the screen is static and doesn't have any dynamic data.
 */
class AboutActivity : ComponentActivity() {

    private fun onNavigationBack() {
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AboutScreen(
                onNavigateBack = { onNavigationBack() },
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
        name = "Gonçalo Ribeiro",
        imageId = R.drawable.ic_user_img,
        socials = socialsDefault("GoncaloRibeiro6533"),
        email = "A48305@alunos.isel.pt"
    ),
    CreatorInfo(
        name = "João Marques",
        imageId = R.drawable.ic_user_img,
        socials = socialsDefault("joaorvm"),
        email = "A48297@alunos.isel.pt"
    )
)


private const val EMAIL_SUBJECT = "About the Channel and Messages App"