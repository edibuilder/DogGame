package com.example.doggame.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.example.doggame.R
import com.example.doggame.model.Facing
import com.example.doggame.model.FallingKind

class GameAssets private constructor(
    val background: Bitmap,
    private val dogSprites: Map<Facing, Bitmap>,
    private val itemSprites: Map<FallingKind, Bitmap>
) {

    fun dogSprite(facing: Facing): Bitmap = dogSprites.getValue(facing)

    fun itemSprite(kind: FallingKind): Bitmap = itemSprites.getValue(kind)

    companion object {

        fun load(resources: Resources, layout: GameLayout): GameAssets {
            fun item(@DrawableRes id: Int) =
                decodeScaled(resources, id, layout.itemWidth, layout.itemHeight)

            fun dog(@DrawableRes id: Int) =
                decodeScaled(resources, id, layout.dogWidth, layout.dogHeight)

            return GameAssets(
                background = decodeScaled(
                    resources, R.drawable.background, layout.screenWidth, layout.screenHeight
                ),
                dogSprites = mapOf(
                    Facing.LEFT to dog(R.drawable.dog_walk_left),
                    Facing.RIGHT to dog(R.drawable.dog_walk_right)
                ),
                itemSprites = mapOf(
                    FallingKind.BISCUIT to item(R.drawable.biscuit),
                    FallingKind.GIFT_BONE to item(R.drawable.gift_bone),
                    FallingKind.BEE to item(R.drawable.bee)
                )
            )
        }

        private fun decodeScaled(
            resources: Resources,
            @DrawableRes id: Int,
            width: Int,
            height: Int
        ): Bitmap {
            val options = BitmapFactory.Options().apply { inScaled = false }
            val source = BitmapFactory.decodeResource(resources, id, options)
            val scaled = Bitmap.createScaledBitmap(source, width, height, true)
            if (scaled !== source) source.recycle()
            return scaled
        }
    }
}
