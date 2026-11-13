package com.example.doggame.model

enum class FallingKind(val points: Int, val speed: Int) {
    BISCUIT(points = 5, speed = 20),
    GIFT_BONE(points = 25, speed = 25),
    BEE(points = -20, speed = 30)
}
