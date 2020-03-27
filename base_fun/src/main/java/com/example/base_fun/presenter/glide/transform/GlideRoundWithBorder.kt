package com.example.base_fun.presenter.glide.transform

import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class GlideRoundWithBorder : BitmapTransformation {
    /**
     * 用一个整形表示哪些边角需要加圆角边框
     * 例如：0b1000,表示左上角需要加圆角边框
     * 0b1110 表示左上右上右下需要加圆角边框
     * 0b0000表示不加圆形边框
     */
    private var radius = 0 //圆角半径
    private var margin = 0 //边距

    private var borderWidth = 0//边框宽度
    private var borderColor = 0//边框颜色
//    private var cornerPos:IntArray? = null//圆角位置

    constructor(
        radius: Int,
        margin: Int,
        borderWidth: Int,
        borderColor: Int//,
//        position: IntArray
    ) {
        this.radius = radius;
        this.margin = margin;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
//        this.cornerPos = position;
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val source: Bitmap = toTransform

        val width = source.width
        val height = source.height

        var bitmap: Bitmap? = pool.get(width, height, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!) //新建一个空白的bitmap

        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(
            source,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        ) //设置要绘制的图形

        val borderPaint = Paint(ANTI_ALIAS_FLAG) //设置边框样式

        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = this.borderWidth.toFloat()

        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat(), borderPaint)
        return bitmap
    }

    private fun drawRoundRect(
        canvas: Canvas,
        paint: Paint,
        width: Float,
        height: Float,
        borderPaint: Paint
    ) {
        val right: Float = width - margin
        val bottom: Float = height - margin
        val halfBorder: Float = borderWidth / 2f
        val path = Path()

        val pos = FloatArray(8)
        for (index in (0..7)) {
            pos[index] = radius.toFloat()
        }
//        var shift: Int = cornerPos
//
//        var index = 3
//
//        while (index >= 0) { //设置四个边角的弧度半径
//            pos[2 * index + 1] = if (shift and 1 > 0) radius.toFloat() else 0f
//            pos[2 * index] = if (shift and 1 > 0) radius.toFloat() else 0f
//            shift = shift shr 1
//            index--
//        }


//        path.addRoundRect(
//            RectF(
//                margin + halfBorder,
//                margin + halfBorder,
//                right - halfBorder,
//                bottom - halfBorder
//            ),
//            pos
//            , Path.Direction.CW
//        )

        canvas.drawPath(path, paint) //绘制要加载的图形
        canvas.drawPath(path, borderPaint) //绘制边框
    }

}