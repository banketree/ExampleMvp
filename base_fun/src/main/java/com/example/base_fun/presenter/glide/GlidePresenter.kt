@file:Suppress("DEPRECATION")

package com.example.base_fun.presenter.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.base_fun.presenter.glide.transform.AvatarCircleCrop
import com.example.base_fun.presenter.glide.transform.GlideRoundTransform
import com.example.base_fun.presenter.glide.transform.GlideRoundWithBorder
import com.example.base_fun.utils.FileUtils
import com.example.base_lib.listener.ICallBackResultListener
import com.thinkcore.cache.memory.impl.UsingFreqLimitedMemoryCache
import com.thinkcore.kandroid.saveBitmap


/**
 * @author banketree
 * @time 2020/1/9 14:55
 * @description
 * 根据业务 加载不一样图片
 */
object GlidePresenter {
    private val cache = UsingFreqLimitedMemoryCache(1024 * 1024)
    private fun getWidthKey(filePath: String): String {
        return filePath + "_width"
    }

    private fun getHeightKey(filePath: String): String {
        return filePath + "_height"
    }

    fun recordWidthHeight(context: Context, imgUrlList: List<String>) {
        if (imgUrlList.isEmpty()) return
        for (imgUrl in imgUrlList) {
            if (cache[getWidthKey(
                    imgUrl
                )] != null && cache[getHeightKey(
                    imgUrl
                )] != null
            ) return
            Glide.with(context).asBitmap().load(imgUrl)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        cache.put(
                            getWidthKey(
                                imgUrl
                            ), resource.width
                        )
                        cache.put(
                            getHeightKey(
                                imgUrl
                            ), resource.height
                        )
                    }
                })
        }
    }

    fun clearRecord() {
        cache.clear()
    }

    /**
     * @param
     * @return
     * @author banketree
     * @time 2020/3/7 15:01
     * @description
     * 下载标准图片
     */
    fun preload(context: Context, imgUrl: String) {
        Glide.with(context.applicationContext).load(imgUrl).preload()
    }

    /**
     * @param
     * @return
     * @author banketree
     * @time 2020/1/9 15:01
     * @description
     * 加载标准图片
     */
    fun loadStandard(img: ImageView, imgUrl: String) {
        Glide.with(img.context.applicationContext).load(imgUrl)
            .into(img)
    }

    /**
     * @param
     * @return
     * @author banketree
     * @time 2020/3/12 14:56
     * @description
     * 圆形图片。
     */
    fun loadCircleCorners(img: ImageView, imgUrl: String) {
        Glide.with(img.context.applicationContext).load(imgUrl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    img.setImageBitmap(null)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(img)
    }

    /**
     * @param
     * @return
     * @author banketree
     * @time 2020/1/9 14:56
     * @description
     * 四周都是圆角的圆角矩形图片。
     */
    fun loadRoundedCorners(img: ImageView, imgUrl: String, roundingRadius: Int = 20) {
        Glide.with(img.context.applicationContext).load(imgUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(roundingRadius)))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    img.setImageBitmap(null)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(img)
    }

    fun loadRoundedCornersWithBorder(
        img: ImageView,
        imgUrl: String,
        roundingRadius: Int = 20,
        borderWidth: Int = 5,
        borderColor: Int = Color.RED
    ) {
//        Glide.with(img.context.applicationContext).load(imgUrl)
//            .apply(
//                RequestOptions.bitmapTransform(
//                    GlideRoundWithBorder(
//                        img.context.applicationContext,
//                        roundingRadius,
//                        0,
//                        borderWidth,
//                        borderColor
//                    )
//                )
//            )//RoundedCorners(roundingRadius)
//            .into(img)

        Glide.with(img.context.applicationContext).load(imgUrl)
            .apply(
                RequestOptions.bitmapTransform(
                    GlideRoundWithBorder(
                        roundingRadius,
                        0,
                        borderWidth,
                        borderColor
                    )
                )
            )
            .into(img)
    }

    fun loadRoundedCenterCrop(img: ImageView, imgUrl: String, roundingRadius: Float = 15f) {
        //获取图片显示在ImageView后的宽高
        Glide.with(img.context.applicationContext).load(imgUrl)
            .apply(RequestOptions.bitmapTransform(GlideRoundTransform(roundingRadius)))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let {
                        val contentHeight =
                            (img.height - img.paddingTop - img.paddingBottom).toFloat()
//                        float scale =(float) vw /(float) resource . getIntrinsicWidth ();

                        cache.put(
                            getWidthKey(
                                imgUrl
                            ), it.intrinsicWidth
                        )
                        cache.put(
                            getHeightKey(
                                imgUrl
                            ), it.intrinsicHeight
                        )
                    }
                    return false
                }
            })
            .into(img)
    }

    /**
     * @param
     * @return
     * @author banketree
     * @time 2020/1/9 14:56
     * @description
     * 加载视频 预览图 第1秒
     */
    fun loadVideoFrame(img: ImageView, videoUrl: String, savePath: String? = "") {
//        Glide.with(img.context.applicationContext)
//            .setDefaultRequestOptions(
//                RequestOptions()
//                    .frame(0) //单位 毫秒
//                    .priority(Priority.HIGH)
//                    .fitCenter()
//            )
//            .load(videoUrl).into(img)
        Glide.with(img.context.applicationContext).setDefaultRequestOptions(
            RequestOptions()
                .frame(0) //单位 毫秒
                .priority(Priority.HIGH)
                .fitCenter()
        ).asBitmap()
            .load(videoUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    if (TextUtils.isEmpty(savePath)) return
                    resource.saveBitmap(savePath!!)
                    img.setImageBitmap(resource)
                    cache.put(
                        getWidthKey(
                            savePath
                        ), resource.width
                    )
                    cache.put(
                        getHeightKey(
                            savePath
                        ), resource.height
                    )
                }
            })
    }


    /**
     * @param
     * @return
     * @author banketree
     * @time 2020/3/13 12:56
     * @description
     * 头像图片。
     */
    fun loadAvatar(img: ImageView, imgUrl: String, size: Int? = null) {

        size?.let {
            Glide.with(img.context.applicationContext).load(imgUrl)
                .apply(RequestOptions.bitmapTransform(AvatarCircleCrop()).override(it))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        img.setImageBitmap(null)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(img)
            return
        }

        Glide.with(img.context.applicationContext).asBitmap().load(imgUrl)
            .apply(RequestOptions.bitmapTransform(AvatarCircleCrop()))
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    img.setImageBitmap(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    img.setImageBitmap(null)
                }
            })
    }

    /**
     * @param
     * @return
     * @author banketree
     * @time 2020/1/9 15:01
     * @description
     * 加载标准图片
     */
    fun saveBitmap(
        context: Context,
        imgUrl: String,
        iCallBackResultListener: ICallBackResultListener?
    ) {
        Glide.with(context.applicationContext).asBitmap().load(imgUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val result = FileUtils.saveBitmapAndAlbum(context, resource)
                    iCallBackResultListener?.onCallBack(result)
                }
            })
    }

    //TODO 须增加 图片宽高 自适应
    //https://www.jianshu.com/p/6cf37941e55f
    //https://blog.csdn.net/qq939782569/article/details/60135790

    fun getImageWidthHeight(pathUrl: String): List<Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val bitmap = BitmapFactory.decodeFile(pathUrl, options)
        if (bitmap != null) {
            val width = bitmap.width
            val height = bitmap.height
            bitmap.recycle()
            return listOf(width, height)
        }
        val width = cache.get(
            getWidthKey(pathUrl)
        ) as? Int
        val height = cache.get(
            getHeightKey(pathUrl)
        ) as? Int
        return listOf(width ?: 0, height ?: 0)
    }

    fun getVideoFirstBitmap(pathUrl: String): Bitmap? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        var resultBitmap: Bitmap? = null
        try {
            //根据url获取缩略图
            mediaMetadataRetriever.setDataSource(pathUrl, HashMap())
            //获得第一帧图片
            resultBitmap = mediaMetadataRetriever.frameAtTime
        } catch (ex: Exception) {
        } finally {
            mediaMetadataRetriever.release()
        }

        return resultBitmap
    }
}

inline fun ImageView.loadStandard(url: String) {
    GlidePresenter.loadStandard(this, url)
}

inline fun ImageView.loadRoundedCorners(url: String, roundingRadius: Int = 30) {
    GlidePresenter.loadRoundedCorners(
        this,
        url,
        roundingRadius
    )
}

inline fun ImageView.loadRoundedCornersWithBorder(
    url: String,
    roundingRadius: Int = 20,
    borderWidth: Int = 5,
    borderColor: Int = Color.RED
) {
    GlidePresenter.loadRoundedCornersWithBorder(
        this,
        url,
        roundingRadius,
        borderWidth,
        borderColor
    )
}

inline fun ImageView.loadRoundedCenterCrop(
    url: String,
    roundingRadius: Float = 15f
) {
    GlidePresenter.loadRoundedCenterCrop(
        this,
        url,
        roundingRadius
    )
}

inline fun ImageView.loadVideoFrame(url: String, savePath: String? = "") {
    GlidePresenter.loadVideoFrame(
        this,
        url,
        savePath
    )
}

inline fun ImageView.saveBitmap(
    url: String,
    iCallBackResultListener: ICallBackResultListener? = null
) {
    GlidePresenter.saveBitmap(
        this.context,
        url,
        iCallBackResultListener
    )
}

inline fun ImageView.loadAvatar(imgUrl: String) {
    GlidePresenter.loadAvatar(this, imgUrl)
}

inline fun ImageView.loadAvatarSmall(imgUrl: String) {
    GlidePresenter.loadAvatar(this, imgUrl, 200)
}