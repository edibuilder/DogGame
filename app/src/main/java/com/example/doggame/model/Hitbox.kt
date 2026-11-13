package com.example.doggame.model

data class Hitbox(val left: Int, val top: Int, val right: Int, val bottom: Int) {

    fun intersects(other: Hitbox): Boolean =
        left < other.right && other.left < right && top < other.bottom && other.top < bottom
}
