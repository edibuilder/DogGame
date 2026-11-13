package com.example.doggame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggame.datamanager.AppDatabase
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.datamanager.User
import com.example.doggame.ui.components.DogBadge
import com.example.doggame.ui.components.FormMessage
import com.example.doggame.ui.components.MenuScreen
import com.example.doggame.ui.components.MenuSubtitle
import com.example.doggame.ui.components.MenuTextField
import com.example.doggame.ui.components.MenuTitle
import com.example.doggame.ui.components.PrimaryMenuButton
import com.example.doggame.ui.theme.DogGameTheme

class LogInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

@Composable
fun LoginScreen() {
    DogGameTheme(dynamicColor = false) {
        MenuScreen {
            LoginForm()
        }
    }
}

@Composable
fun LoginForm() {
    val context = LocalContext.current

    val invalidUsernameMessage: String = stringResource(R.string.not_existing_user_message)
    val invalidPasswordMessage: String = stringResource(R.string.wrong_password_message)

    var textMessage by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    DogBadge()
    MenuTitle(stringResource(R.string.log_in))
    MenuSubtitle(stringResource(R.string.login_subtitle))
    Spacer(Modifier.height(8.dp))

    MenuTextField(
        value = username.trim(),
        onValueChange = { username = it.lowercase() },
        label = stringResource(R.string.username)
    )

    MenuTextField(
        value = password.trim(),
        onValueChange = { password = it },
        label = stringResource(R.string.password),
        isPassword = true
    )

    FormMessage(text = textMessage, isError = true)

    Spacer(Modifier.height(4.dp))

    PrimaryMenuButton(
        text = stringResource(R.string.log_in),
        onClick = {
            val user: User? = searchUserByUsername(username, context)
            if (user == null) {
                textMessage = invalidUsernameMessage
            } else if (password != user.password) {
                textMessage = invalidPasswordMessage
            } else {
                LoggedUser.login(user)
                context.startActivity(Intent(context, MenuActivity::class.java))
            }
        }
    )
}

fun searchUserByUsername(username: String, context: Context) : User? {
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()
    var user: User? = null

    try {
        user = userDao.findByUsername(username)
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error reading from database", e)
    }

    return user
}
