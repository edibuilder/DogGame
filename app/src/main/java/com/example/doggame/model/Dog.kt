package com.example.doggame.model

enum class Facing { LEFT, RIGHT }

class Dog(x: Int, y: Int, width: Int, height: Int) : GameObject(x, y, width, height) {

    var facing: Facing = Facing.LEFT
        private set

    fun moveBy(dx: Int, minX: Int, maxX: Int) {
        if (dx == 0) return
        facing = if (dx > 0) Facing.RIGHT else Facing.LEFT
        x = (x + dx).coerceIn(minX, maxX)
    }
}
