package com.example.doggame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggame.ui.components.DogBadge
import com.example.doggame.ui.components.MenuScreen
import com.example.doggame.ui.components.MenuSubtitle
import com.example.doggame.ui.components.MenuTitle
import com.example.doggame.ui.components.PrimaryMenuButton
import com.example.doggame.ui.components.SecondaryMenuButton
import com.example.doggame.ui.theme.DogGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    DogGameTheme(dynamicColor = false) {
        MenuScreen {
            DogBadge()
            MenuTitle(stringResource(R.string.app_name))
            MenuSubtitle(stringResource(R.string.tagline))
            Spacer(Modifier.height(8.dp))
            PrimaryMenuButton(
                text = stringResource(R.string.log_in),
                onClick = { context.startActivity(Intent(context, LogInActivity::class.java)) }
            )
            SecondaryMenuButton(
                text = stringResource(R.string.register),
                onClick = { context.startActivity(Intent(context, RegisterActivity::class.java)) }
            )
        }
    }
}
