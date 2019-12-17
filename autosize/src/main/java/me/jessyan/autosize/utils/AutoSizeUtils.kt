package me.jessyan.autosize.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.app.Application

import java.lang.reflect.InvocationTargetException

/**
 * AndroidAutoSize 常用工具类
 */
class AutoSizeUtils private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {

        fun dp2px(context: Context, value: Float): Int {
            return (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }

        fun sp2px(context: Context, value: Float): Int {
            return (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }

        fun pt2px(context: Context, value: Float): Int {
            return (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PT,
                value,
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }

        fun in2px(context: Context, value: Float): Int {
            return (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_IN,
                value,
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }

        fun mm2px(context: Context, value: Float): Int {
            return (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_MM,
                value,
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }

        val applicationByReflect: Application
            get() {
                try {
                    @SuppressLint("PrivateApi")
                    val activityThread = Class.forName("android.app.ActivityThread")
                    val thread = activityThread.getMethod("currentActivityThread").invoke(null)
                    val app = activityThread.getMethod("getApplication").invoke(thread)
                        ?: throw NullPointerException("you should init first")
                    return app as Application
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }

                throw NullPointerException("you should init first")
            }
    }
}
