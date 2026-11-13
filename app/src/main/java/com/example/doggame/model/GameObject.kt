package com.example.doggame.model

open class GameObject(
    var x: Int,
    var y: Int,
    val width: Int,
    val height: Int
) {

    fun hitbox(): Hitbox {
        val inset = (minOf(width, height) * HITBOX_INSET_RATIO).toInt()
        return Hitbox(x + inset, y + inset, x + width - inset, y + height - inset)
    }

    private companion object {
        const val HITBOX_INSET_RATIO = 0.2f
    }
}
