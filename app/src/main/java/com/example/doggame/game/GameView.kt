package com.example.doggame.game

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.doggame.R
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class GameView(
    context: Context,
    private val onGameOver: (score: Int) -> Unit
) : SurfaceView(context), SurfaceHolder.Callback {

    private var engine: GameEngine? = null
    private var renderer: GameRenderer? = null
    private var loop: GameLoop? = null
    private var lastTouchX = 0f

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) = Unit

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (width <= 0 || height <= 0) return
        if (engine == null) setUpGame(width, height)
        startLoop(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        loop?.shutdown()
        loop = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val engine = engine ?: return true
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> lastTouchX = event.x
            MotionEvent.ACTION_MOVE -> {
                val dx = (event.x - lastTouchX).roundToInt()
                lastTouchX = event.x
                synchronized(engine) { engine.moveDogBy(dx) }
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

    private fun startLoop(holder: SurfaceHolder) {
        val engine = engine ?: return
        val renderer = renderer ?: return
        if (loop?.isAlive == true || engine.isOver) return
        loop = GameLoop(holder, engine, renderer) { score -> post { onGameOver(score) } }
            .also { it.start() }
    }

    private companion object {
        const val HUD_TEXT_SIZE_SP = 20f
    }
}
