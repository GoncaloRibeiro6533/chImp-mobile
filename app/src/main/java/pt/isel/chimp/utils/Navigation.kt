package pt.isel.chimp.utils

import android.content.Intent
import androidx.activity.ComponentActivity


fun navigateTo(origin: ComponentActivity, destination: Class<*>) {
    val intent = Intent(origin, destination)
    origin.startActivity(intent)
}