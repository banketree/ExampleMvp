@file:Suppress("NOTHING_TO_INLINE")

package com.example.base_lib.kandroid


/**
 * 距今多少天了
 * */
inline fun Number.getDaysSoFar(): Int {
    val second = (System.currentTimeMillis() - toLong()) / 1000
    val days = second / (24 * 60 * 60)
    return (days).toInt()
}