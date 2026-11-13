package com.example.doggame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doggame.datamanager.AppDatabase
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.ui.theme.DogGameTheme

class GameOverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val lastScore = LoggedUser.getLastScore() ?: 0
        val bestScore = maxOf(LoggedUser.getBestScore(), lastScore)
        if (savedInstanceState == null) saveBestScore(this, bestScore)

        setContent {
            GameOverScreen(lastScore = lastScore, bestScore = bestScore)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverPreview() {
    GameOverScreen(lastScore = 120, bestScore = 340)
}

@Composable
fun GameOverScreen(lastScore: Int, bestScore: Int) {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GameOverMenuButtons(
                lastScore = lastScore,
                bestScore = bestScore,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun GameOverMenuButtons(lastScore: Int, bestScore: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.game_over),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Text(text = stringResource(R.string.score) + ": $lastScore")

        Text(text = stringResource(R.string.best_score) + ": $bestScore")

        Button(
            onClick = {
                context.startActivity(Intent(context, StartGameActivity::class.java))
                (context as? Activity)?.finish()
            }
        ) {
            Text(text = stringResource(R.string.play_again), fontSize = 20.sp)
        }

        Button(
            onClick = {
                val intent = Intent(context, MenuActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
                (context as? Activity)?.finish()
            }
        ) {
            Text(text = stringResource(R.string.main_menu), fontSize = 20.sp)
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
