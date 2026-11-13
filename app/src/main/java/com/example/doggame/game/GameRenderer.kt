package com.example.doggame.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class GameRenderer(
    private val assets: GameAssets,
    private val scoreLabel: String,
    private val missedGiftsLabel: String,
    textSize: Float
) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        this.textSize = textSize
        setShadowLayer(4f, 0f, 0f, Color.BLACK)
    }

    private val hudMargin = textSize
    private val hudLineHeight = textSize * 1.3f

    fun draw(canvas: Canvas, engine: GameEngine) {
        canvas.drawBitmap(assets.background, 0f, 0f, null)
        drawFallingObjects(canvas, engine)
        drawDog(canvas, engine)
        drawHud(canvas, engine)
    }

    private fun drawFallingObjects(canvas: Canvas, engine: GameEngine) {
        for (item in engine.falling) {
            canvas.drawBitmap(assets.itemSprite(item.kind), item.x.toFloat(), item.y.toFloat(), null)
        }
    }

    private fun drawDog(canvas: Canvas, engine: GameEngine) {
        val dog = engine.dog
        canvas.drawBitmap(assets.dogSprite(dog.facing), dog.x.toFloat(), dog.y.toFloat(), null)
    }

    private fun drawHud(canvas: Canvas, engine: GameEngine) {
        val top = hudMargin + hudLineHeight
        canvas.drawText("$scoreLabel: ${engine.score}", hudMargin, top, textPaint)
        canvas.drawText(
            "$missedGiftsLabel: ${engine.missedGifts}/${GameConfig.MAX_MISSED_GIFTS}",
            hudMargin,
            top + hudLineHeight,
            textPaint
        )
    }
}
