package com.example.doggame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.ui.components.DogBadge
import com.example.doggame.ui.components.MenuScreen
import com.example.doggame.ui.components.MenuSubtitle
import com.example.doggame.ui.components.MenuTitle
import com.example.doggame.ui.components.PrimaryMenuButton
import com.example.doggame.ui.components.SecondaryMenuButton
import com.example.doggame.ui.theme.DogGameTheme

class MenuActivity : ComponentActivity() {

    private var bestScore by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainMenuScreen(username = LoggedUser.getUsername(), bestScore = bestScore)
        }
    }

    override fun onResume() {
        super.onResume()
        bestScore = LoggedUser.getBestScore()
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    MainMenuScreen(username = "edvin", bestScore = 700)
}

@Composable
fun MainMenuScreen(username: String?, bestScore: Int) {
    val context = LocalContext.current

    DogGameTheme(dynamicColor = false) {
        MenuScreen {
            DogBadge()
            MenuTitle(stringResource(R.string.main_menu))
            if (username != null) {
                MenuSubtitle(stringResource(R.string.welcome_user, username))
            }
            MenuSubtitle(stringResource(R.string.best_score_value, bestScore))
            Spacer(Modifier.height(8.dp))
            PrimaryMenuButton(
                text = stringResource(R.string.start_game),
                onClick = { context.startActivity(Intent(context, StartGameActivity::class.java)) }
            )
            SecondaryMenuButton(
                text = stringResource(R.string.dashboard),
                onClick = { context.startActivity(Intent(context, DashboardActivity::class.java)) }
            )
            SecondaryMenuButton(
                text = stringResource(R.string.profile),
                onClick = { context.startActivity(Intent(context, ProfileActivity::class.java)) }
            )
        }
    }
}
