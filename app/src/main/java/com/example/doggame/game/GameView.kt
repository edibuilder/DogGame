package com.example.doggame.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.TypedValue
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import com.example.doggame.R
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class GameView(
    context: Context,
    private val onGameOver: (score: Int) -> Unit
) : View(context), Choreographer.FrameCallback {

    private var engine: GameEngine? = null
    private var renderer: GameRenderer? = null

    private var running = false
    private var finished = false
    private var lastFrameNanos = 0L
    private var pendingMs = 0.0
    private var lastTouchX = 0f

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (engine == null && width > 0 && height > 0) setUpGame(width, height)
        resume()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) resume() else pause()
    }

    override fun onDetachedFromWindow() {
        pause()
        super.onDetachedFromWindow()
    }

    override fun doFrame(frameTimeNanos: Long) {
        val engine = engine
        if (!running || engine == null) return

        if (lastFrameNanos != 0L) {
            pendingMs += (frameTimeNanos - lastFrameNanos) / NANOS_PER_MS
            pendingMs = pendingMs.coerceAtMost(STEP_MS * MAX_STEPS_PER_FRAME)
        }
        lastFrameNanos = frameTimeNanos

        while (pendingMs >= STEP_MS && !engine.isOver) {
            engine.update()
            pendingMs -= STEP_MS
        }
        invalidate()

        if (engine.isOver) {
            finished = true
            pause()
            onGameOver(engine.score)
        } else {
            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val engine = engine ?: return
        renderer?.draw(canvas, engine)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val engine = engine ?: return true
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> lastTouchX = event.x
            MotionEvent.ACTION_MOVE -> {
                engine.moveDogBy((event.x - lastTouchX).roundToInt())
                lastTouchX = event.x
            }
        }
        return true
    }

    private fun setUpGame(width: Int, height: Int) {
        val layout = GameLayout(width, height)
        engine = GameEngine(layout)
        renderer = GameRenderer(
            assets = GameAssets.load(resources, layout),
            scoreLabel = context.getString(R.string.score),
            missedGiftsLabel = context.getString(R.string.missed_gifts),
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, HUD_TEXT_SIZE_SP, resources.displayMetrics
            )
        )
    }

    private fun resume() {
        if (running || finished || engine == null) return
        running = true
        lastFrameNanos = 0L
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun pause() {
        running = false
        Choreographer.getInstance().removeFrameCallback(this)
    }

    private companion object {
        const val HUD_TEXT_SIZE_SP = 20f
        const val NANOS_PER_MS = 1_000_000.0
        val STEP_MS = GameConfig.FRAME_TIME_MS.toDouble()
        const val MAX_STEPS_PER_FRAME = 5
    }
}
