package com.example.base_fun.presenter.glide.transform

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class GlideCircleWithBorder : BitmapTransformation {
    private lateinit var borderPaint: Paint
    private var borderWidth = 0f

    constructor(
        context: Context,
        borderWidth: Int,
        borderColor: Int
    ) {
//        super(context)
        this.borderWidth = Resources.getSystem().displayMetrics.density * borderWidth
        borderPaint = Paint()
        borderPaint.isDither = true
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = this.borderWidth
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return circleCrop(pool, toTransform);
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap): Bitmap {
        val size =
            (Math.min(source.width, source.height) - borderWidth / 2) as Int
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        //创建画笔 画布 手动描绘边框
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(
            squared,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )

        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        if (borderPaint != null) {
            val borderRadius: Float = r - borderWidth / 2
            canvas.drawCircle(r, r, borderRadius, borderPaint)
        }
        return result
    }

}