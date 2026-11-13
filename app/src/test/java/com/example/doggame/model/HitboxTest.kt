package com.example.doggame.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HitboxTest {

    @Test
    fun overlappingBoxesIntersect() {
        val a = Hitbox(0, 0, 100, 100)
        val b = Hitbox(50, 50, 150, 150)

        assertTrue(a.intersects(b))
        assertTrue(b.intersects(a))
    }

    @Test
    fun touchingEdgesDoNotIntersect() {
        val a = Hitbox(0, 0, 100, 100)
        val b = Hitbox(100, 0, 200, 100)

        assertFalse(a.intersects(b))
    }

    @Test
    fun separateBoxesDoNotIntersect() {
        val a = Hitbox(0, 0, 100, 100)
        val b = Hitbox(0, 300, 100, 400)

        assertFalse(a.intersects(b))
    }

    @Test
    fun hitboxIsInsetFromObjectBounds() {
        val box = GameObject(x = 10, y = 20, width = 100, height = 50).hitbox()

        assertTrue(box.left > 10)
        assertTrue(box.top > 20)
        assertTrue(box.right < 110)
        assertTrue(box.bottom < 70)
    }
}
