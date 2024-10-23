package pt.isel.chimp.auth.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class RegisterActivity: ComponentActivity() {

    private val viewModel by viewModels<RegisterScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }

    }
}