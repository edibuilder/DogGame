package com.example.doggame.model

class FallingObject(
    val kind: FallingKind,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : GameObject(x, y, width, height) {

    fun fall() {
        y += kind.speed
    }
}
