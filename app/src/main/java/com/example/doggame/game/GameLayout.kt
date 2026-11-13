package com.example.doggame.game

data class GameLayout(val screenWidth: Int, val screenHeight: Int) {

    init {
        require(screenWidth > 0 && screenHeight > 0) { "Screen size must be positive" }
    }

    val dogWidth: Int = screenWidth / 3
    val dogHeight: Int = screenHeight / 6
    val dogX: Int = (screenWidth - dogWidth) / 2
    val dogY: Int = screenHeight / 2 + dogHeight * 3 / 4

    val itemWidth: Int = screenWidth / 7
    val itemHeight: Int = screenHeight / 13
}
