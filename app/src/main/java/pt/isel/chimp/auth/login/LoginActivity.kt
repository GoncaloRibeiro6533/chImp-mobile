package pt.isel.chimp.auth.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class LoginActivity: ComponentActivity() {

    private val viewModel by viewModels<LoginScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }

    }
}