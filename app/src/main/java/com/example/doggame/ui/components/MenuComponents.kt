package com.example.doggame.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.doggame.R
import com.example.doggame.ui.theme.Bark
import com.example.doggame.ui.theme.BarkMuted
import com.example.doggame.ui.theme.Butter
import com.example.doggame.ui.theme.Card
import com.example.doggame.ui.theme.Coral

private val CardShape = RoundedCornerShape(28.dp)
private val ButtonShape = RoundedCornerShape(16.dp)
private val ButtonHeight = 52.dp

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, Bark.copy(alpha = 0.35f)))
                )
        )
        Surface(
            shape = CardShape,
            color = Card,
            shadowElevation = 12.dp,
            modifier = Modifier
                .align(Alignment.Center)
                .safeDrawingPadding()
                .padding(24.dp)
                .widthIn(max = 420.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

@Composable
fun DogBadge(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.dog_icon),
        contentDescription = null,
        modifier = modifier.size(96.dp)
    )
}

@Composable
fun MenuTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = Bark,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun MenuSubtitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = BarkMuted,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun MenuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = ButtonShape,
        visualTransformation =
            if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Bark,
            unfocusedTextColor = Bark,
            focusedBorderColor = Coral,
            unfocusedBorderColor = BarkMuted.copy(alpha = 0.5f),
            focusedLabelColor = Coral,
            unfocusedLabelColor = BarkMuted,
            cursorColor = Coral
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun FormMessage(text: String, isError: Boolean, modifier: Modifier = Modifier) {
    if (text.isEmpty()) return
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = if (isError) Coral else BarkMuted,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun PrimaryMenuButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    MenuButton(text, onClick, Coral, Color.White, modifier)
}

@Composable
fun SecondaryMenuButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    MenuButton(text, onClick, Butter, Bark, modifier)
}

@Composable
private fun MenuButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}
