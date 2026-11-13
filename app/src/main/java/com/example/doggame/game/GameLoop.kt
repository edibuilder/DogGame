package com.example.doggame.game

import android.os.SystemClock
import android.view.SurfaceHolder

class GameLoop(
    private val holder: SurfaceHolder,
    private val engine: GameEngine,
    private val renderer: GameRenderer,
    private val onGameOver: (score: Int) -> Unit
) : Thread("GameLoop") {

    @Volatile
    private var running = true

    fun shutdown() {
        running = false
        join()
    }

    override fun run() {
        while (running && !isGameOver()) {
            val frameStart = SystemClock.uptimeMillis()
            synchronized(engine) { engine.update() }
            render()
            val remaining = GameConfig.FRAME_TIME_MS - (SystemClock.uptimeMillis() - frameStart)
            if (remaining > 0) Thread.sleep(remaining)
        }
        if (running) onGameOver(synchronized(engine) { engine.score })
    }

    private fun isGameOver(): Boolean = synchronized(engine) { engine.isOver }

    private fun render() {
        val canvas = holder.lockCanvas() ?: return
        try {
            synchronized(engine) { renderer.draw(canvas, engine) }
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }
}
