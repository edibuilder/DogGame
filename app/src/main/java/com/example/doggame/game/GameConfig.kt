package com.example.doggame.game

import com.example.doggame.model.FallingKind

data class Level(
    val minScore: Int,
    val biscuitInterval: Int,
    val giftBoneInterval: Int,
    val beeInterval: Int
) {

    fun intervalFor(kind: FallingKind): Int = when (kind) {
        FallingKind.BISCUIT -> biscuitInterval
        FallingKind.GIFT_BONE -> giftBoneInterval
        FallingKind.BEE -> beeInterval
    }
}

object GameConfig {
    const val FRAME_TIME_MS = 40L
    const val MAX_MISSED_GIFTS = 5

    val levels: List<Level> = listOf(
        Level(minScore = 0, biscuitInterval = 45, giftBoneInterval = 130, beeInterval = 170),
        Level(minScore = 200, biscuitInterval = 45, giftBoneInterval = 100, beeInterval = 130),
        Level(minScore = 500, biscuitInterval = 40, giftBoneInterval = 80, beeInterval = 100),
        Level(minScore = 1000, biscuitInterval = 35, giftBoneInterval = 60, beeInterval = 70)
    )

    fun levelFor(score: Int): Level = levels.last { score >= it.minScore }
}
