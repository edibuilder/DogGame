package com.example.doggame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggame.datamanager.AppDatabase
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.ui.components.MenuScreen
import com.example.doggame.ui.components.MenuSubtitle
import com.example.doggame.ui.components.MenuTitle
import com.example.doggame.ui.components.PrimaryMenuButton
import com.example.doggame.ui.components.SecondaryMenuButton
import com.example.doggame.ui.theme.Coral
import com.example.doggame.ui.theme.DogGameTheme

class GameOverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val lastScore = LoggedUser.getLastScore() ?: 0
        val previousBest = LoggedUser.getBestScore()
        val bestScore = maxOf(previousBest, lastScore)
        val isNewBest = lastScore > previousBest
        if (savedInstanceState == null) saveBestScore(this, bestScore)

        setContent {
            GameOverScreen(lastScore = lastScore, bestScore = bestScore, isNewBest = isNewBest)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverPreview() {
    GameOverScreen(lastScore = 120, bestScore = 340, isNewBest = false)
}

@Composable
fun GameOverScreen(lastScore: Int, bestScore: Int, isNewBest: Boolean) {
    val context = LocalContext.current

    DogGameTheme(dynamicColor = false) {
        MenuScreen {
            MenuTitle(stringResource(R.string.game_over))
            Text(
                text = lastScore.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Coral
            )
            MenuSubtitle(
                if (isNewBest) stringResource(R.string.new_best_score)
                else stringResource(R.string.best_score_value, bestScore)
            )
            Spacer(Modifier.height(8.dp))
            PrimaryMenuButton(
                text = stringResource(R.string.play_again),
                onClick = {
                    context.startActivity(Intent(context, StartGameActivity::class.java))
                    (context as? Activity)?.finish()
                }
            )
            SecondaryMenuButton(
                text = stringResource(R.string.main_menu),
                onClick = {
                    val intent = Intent(context, MenuActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}

fun saveBestScore(context: Context, bestScore: Int) {
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()

    try {
        LoggedUser.getUsername()?.let { userDao.updateUserBestScore(it, bestScore) }
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error writing to database", e)
    }

    LoggedUser.setBestScore(bestScore)
}
