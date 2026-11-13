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
import com.example.doggame.datamanager.Validation
import com.example.doggame.ui.components.FormMessage
import com.example.doggame.ui.components.MenuScreen
import com.example.doggame.ui.components.MenuSubtitle
import com.example.doggame.ui.components.MenuTextField
import com.example.doggame.ui.components.MenuTitle
import com.example.doggame.ui.components.PrimaryMenuButton
import com.example.doggame.ui.theme.DogGameTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}

@Composable
fun RegisterScreen() {
    DogGameTheme(dynamicColor = false) {
        MenuScreen {
            RegisterForm()
        }
    }
}

@Composable
fun RegisterForm() {
    val context = LocalContext.current

    val requiredFieldsMessage: String = stringResource(R.string.required_fields_message)
    val invalidUsernameMessage: String = stringResource(R.string.invalid_username_message)
    val invalidPasswordMessage: String = stringResource(R.string.invalid_password_message)
    val unequalPasswordsMessage: String = stringResource(R.string.unequal_passwords_message)

    var textMessage by remember { mutableStateOf(requiredFieldsMessage) }
    var isError by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatedPassword by rememberSaveable { mutableStateOf("") }

    fun showError(message: String) {
        textMessage = message
        isError = true
    }

    MenuTitle(stringResource(R.string.register))
    MenuSubtitle(stringResource(R.string.register_subtitle))
    Spacer(Modifier.height(8.dp))

    MenuTextField(
        value = username.trim(),
        onValueChange = { username = it.lowercase() },
        label = stringResource(R.string.username) + " *"
    )

    MenuTextField(
        value = password.trim(),
        onValueChange = { password = it },
        label = stringResource(R.string.password) + " *",
        isPassword = true
    )

    MenuTextField(
        value = repeatedPassword.trim(),
        onValueChange = { repeatedPassword = it },
        label = stringResource(R.string.repeat_password) + " *",
        isPassword = true
    )

    FormMessage(text = textMessage, isError = isError)

    Spacer(Modifier.height(4.dp))

    PrimaryMenuButton(
        text = stringResource(R.string.register),
        onClick = {
            val validation = Validation()
            if (validation.checkRequiredFields(username, password, repeatedPassword)) {
                showError(requiredFieldsMessage)
            } else if (!validation.isUsernameUnique(username, context)) {
                showError(invalidUsernameMessage)
            } else if (validation.arePasswordsDifferent(password, repeatedPassword)) {
                showError(unequalPasswordsMessage)
            } else if (!validation.isPasswordValid(password)) {
                showError(invalidPasswordMessage)
            } else {
                createUser(username, password, context)
                context.startActivity(Intent(context, MenuActivity::class.java))
            }
        }
    )
}

fun createUser(username: String, password: String, context: Context){
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()
    val user = User(username = username, password = password,
        bestScore = null, avatarImage = null)

    try {
        userDao.insert(user)
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error writing to database", e)
    }

    LoggedUser.login(user)
}
