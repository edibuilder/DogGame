package com.example.doggame.game

import com.example.doggame.model.Facing
import com.example.doggame.model.FallingKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class GameEngineTest {

    private val layout = GameLayout(screenWidth = 1080, screenHeight = 2400)
    private val engine = GameEngine(layout, Random(42))

    private fun dropOnDog(kind: FallingKind) {
        val item = engine.spawn(kind)
        item.x = engine.dog.x
        item.y = engine.dog.y
    }

    private fun letFallPastDog(kind: FallingKind) {
        val item = engine.spawn(kind)
        item.x = 0
        item.y = layout.screenHeight
    }

    @Test
    fun catchingBiscuitAddsPoints() {
        dropOnDog(FallingKind.BISCUIT)
        engine.update()

        assertEquals(FallingKind.BISCUIT.points, engine.score)
        assertTrue(engine.falling.isEmpty())
    }

    @Test
    fun catchingGiftBoneAddsPoints() {
        dropOnDog(FallingKind.GIFT_BONE)
        engine.update()

        assertEquals(FallingKind.GIFT_BONE.points, engine.score)
    }

    @Test
    fun catchingBeeReducesScore() {
        dropOnDog(FallingKind.GIFT_BONE)
        engine.update()
        dropOnDog(FallingKind.BEE)
        engine.update()

        assertEquals(FallingKind.GIFT_BONE.points + FallingKind.BEE.points, engine.score)
    }

    @Test
    fun scoreNeverDropsBelowZero() {
        dropOnDog(FallingKind.BEE)
        engine.update()

        assertEquals(0, engine.score)
    }

    @Test
    fun missedGiftBoneIsCounted() {
        letFallPastDog(FallingKind.GIFT_BONE)
        engine.update()

        assertEquals(1, engine.missedGifts)
        assertTrue(engine.falling.isEmpty())
    }

    @Test
    fun missedBiscuitIsNotCounted() {
        letFallPastDog(FallingKind.BISCUIT)
        engine.update()

        assertEquals(0, engine.missedGifts)
        assertTrue(engine.falling.isEmpty())
    }

    @Test
    fun gameEndsAfterMaxMissedGifts() {
        repeat(GameConfig.MAX_MISSED_GIFTS) {
            assertFalse(engine.isOver)
            letFallPastDog(FallingKind.GIFT_BONE)
            engine.update()
        }

        assertTrue(engine.isOver)
    }

    @Test
    fun updateHasNoEffectAfterGameOver() {
        repeat(GameConfig.MAX_MISSED_GIFTS) {
            letFallPastDog(FallingKind.GIFT_BONE)
            engine.update()
        }
        val item = engine.spawn(FallingKind.BISCUIT)
        val startY = item.y

        engine.update()

        assertEquals(startY, item.y)
    }

    @Test
    fun objectsSpawnOnSchedule() {
        val interval = GameConfig.levelFor(0).biscuitInterval
        repeat(interval - 1) { engine.update() }
        assertTrue(engine.falling.none { it.kind == FallingKind.BISCUIT })

        engine.update()

        assertTrue(engine.falling.any { it.kind == FallingKind.BISCUIT })
    }

    @Test
    fun spawnedObjectsStayWithinScreenWidth() {
        repeat(100) {
            val item = engine.spawn(FallingKind.BISCUIT)
            assertTrue(item.x >= 0)
            assertTrue(item.x + item.width <= layout.screenWidth)
        }
    }

    @Test
    fun dogStaysInsideScreen() {
        engine.moveDogBy(-layout.screenWidth * 2)
        assertEquals(0, engine.dog.x)

        engine.moveDogBy(layout.screenWidth * 2)
        assertEquals(layout.screenWidth - engine.dog.width, engine.dog.x)
    }

    @Test
    fun dogFacesMovementDirection() {
        engine.moveDogBy(10)
        assertEquals(Facing.RIGHT, engine.dog.facing)

        engine.moveDogBy(-10)
        assertEquals(Facing.LEFT, engine.dog.facing)
    }

    @Test
    fun difficultyIncreasesWithScore() {
        val start = GameConfig.levelFor(0)
        val hardest = GameConfig.levelFor(Int.MAX_VALUE)

        assertTrue(hardest.giftBoneInterval < start.giftBoneInterval)
        assertTrue(hardest.beeInterval < start.beeInterval)
    }
}
