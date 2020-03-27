package com.example.base_fun.presenter.glide.transform

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class AvatarCircleCrop : CircleCrop() {
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
//        1125的图片，截取中心800的圆形
        val radiusScale = 0.7f

        return TransformationUtils.circleCrop(
            pool,
            toTransform,
            outWidth,
            outHeight,
            radiusScale
        )

//        return super.transform(pool,toTransform, outWidth, outHeight)
    }

    override fun equals(o: Any?): Boolean {
        return o is AvatarCircleCrop
    }


}