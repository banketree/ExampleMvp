package com.example.base_fun.presenter.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.thinkcore.storage.TStorageUtils
import com.thinkcore.storage.kandroid.getExternalImageDir
import com.thinkcore.storage.kandroid.getInterImageDir


@GlideModule
class GlideCache : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSizeBytes = 1024 * 1024 * 100L // 100 MB
        //设置缓存路径
        if (TStorageUtils.isExternalStoragePresent()) {
            builder.setDiskCache(
                DiskLruCacheFactory(context.getExternalImageDir(), diskCacheSizeBytes)
            )
        }else {
            builder.setDiskCache(
                DiskLruCacheFactory(context.getInterImageDir(), diskCacheSizeBytes)
            )
        }
//        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
//        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
//        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }
}