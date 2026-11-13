package com.example.doggame.game

import com.example.doggame.model.Dog
import com.example.doggame.model.FallingKind
import com.example.doggame.model.FallingObject
import kotlin.random.Random

class GameEngine(
    private val layout: GameLayout,
    private val random: Random = Random.Default
) {

    val dog = Dog(layout.dogX, layout.dogY, layout.dogWidth, layout.dogHeight)

    private val fallingObjects = mutableListOf<FallingObject>()
    val falling: List<FallingObject> get() = fallingObjects

    private val spawners = FallingKind.entries.associateWith { Spawner() }

    var score: Int = 0
        private set

    var missedGifts: Int = 0
        private set

    val isOver: Boolean get() = missedGifts >= GameConfig.MAX_MISSED_GIFTS

    fun update() {
        if (isOver) return
        spawnDue()
        advanceFallingObjects()
    }

    fun moveDogBy(dx: Int) {
        if (isOver) return
        dog.moveBy(dx, minX = 0, maxX = layout.screenWidth - dog.width)
    }

    internal fun spawn(kind: FallingKind): FallingObject {
        val x = random.nextInt(layout.screenWidth - layout.itemWidth + 1)
        val item = FallingObject(kind, x, -layout.itemHeight, layout.itemWidth, layout.itemHeight)
        fallingObjects.add(item)
        return item
    }

    private fun spawnDue() {
        val level = GameConfig.levelFor(score)
        for ((kind, spawner) in spawners) {
            if (spawner.tick(level.intervalFor(kind))) spawn(kind)
        }
    }

    private fun advanceFallingObjects() {
        val dogHitbox = dog.hitbox()
        val iterator = fallingObjects.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            item.fall()
            when {
                item.hitbox().intersects(dogHitbox) -> {
                    collect(item)
                    iterator.remove()
                }
                item.y > layout.screenHeight -> {
                    if (item.kind == FallingKind.GIFT_BONE) missedGifts++
                    iterator.remove()
                }
            }
        }
    }

    private fun collect(item: FallingObject) {
        score = (score + item.kind.points).coerceAtLeast(0)
    }

    private class Spawner {
        private var elapsedFrames = 0

        fun tick(interval: Int): Boolean {
            elapsedFrames++
            if (elapsedFrames < interval) return false
            elapsedFrames = 0
            return true
        }
    }
}
