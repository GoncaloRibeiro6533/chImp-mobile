package pt.isel.chimp.about

import android.net.Uri
import androidx.annotation.DrawableRes
import pt.isel.chimp.R

/**
 * Used to represent information about a social network in the about screen
 * @param imageId the id of the image to be displayed
 */
data class CreatorInfo(
    val name: String,
    @DrawableRes val imageId: Int,
    val socials: List<SocialInfo>,
    val email: String
)

data class SocialInfo(
    val link: Uri,
    @DrawableRes val imageId: Int
)

fun socialsDefault(gitHub: String) = listOf(
    SocialInfo(
        link = Uri.parse("https://github.com/$gitHub"),
        imageId = R.drawable.ic_github
    )
)

