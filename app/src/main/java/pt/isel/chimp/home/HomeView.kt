package pt.isel.chimp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.R

@Composable
fun HomeView(
    onLoginRequested: () -> Unit,
    onRegisterRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.padding(10.dp).size(250.dp),
            painter = painterResource(id = R.drawable.ic_logo_app4),
            contentDescription = "ChIMP logo",
        )
        Text(
            text = "Welcome to ChIMP",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(bottom = 30.dp),
            fontSize = 30.sp
        )
        Button(
            onClick = onLoginRequested,
            modifier = Modifier.width(150.dp).padding(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color.White
                else Color.Black,
            )
        ) {
            Text(text = "Log In")
        }
        Button(
            onClick = onRegisterRequested,
            modifier = Modifier.width(150.dp).padding(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color.White
                else Color.Black,
            )
        ) {
            Text(text = "Register")
        }
    }
}