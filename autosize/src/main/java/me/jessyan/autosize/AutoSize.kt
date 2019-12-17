package me.jessyan.autosize

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import me.jessyan.autosize.external.ExternalAdaptInfo
import me.jessyan.autosize.internal.CustomAdapt
import me.jessyan.autosize.unit.Subunits
import me.jessyan.autosize.utils.LogUtils
import me.jessyan.autosize.utils.Preconditions
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

/**
 * AndroidAutoSize 用于屏幕适配的核心方法都在这里, 核心原理来自于 [今日头条官方适配方案](https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
 * 此方案只要应用到 [Activity] 上, 这个 [Activity] 下的所有 [@Fragment]、[Dialog]、
 * 自定义 [View] 都会达到适配的效果, 如果某个页面不想使用适配请让该 [Activity] 实现 [CancelAdapt]
 *
 *
 */
class AutoSize private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {
        private val mCache = ConcurrentHashMap<String, DisplayMetricsInfo>()

        /**
         * 使用 AndroidAutoSize 初始化时设置的默认适配参数进行适配 (AndroidManifest 的 Meta 属性)
         *
         * @param activity [Activity]
         */
        fun autoConvertDensityOfGlobal(activity: Activity) {
            if (AutoSizeConfig.instance!!.isBaseOnWidth()) {
                autoConvertDensityBaseOnWidth(activity, AutoSizeConfig.instance!!.designWidthInDp as Float)
            } else {
                autoConvertDensityBaseOnHeight(activity, AutoSizeConfig.instance!!.designHeightInDp as Float)
            }
        }

        /**
         * 使用 [Activity] 或 [@Fragment] 的自定义参数进行适配
         *
         * @param activity    [Activity]
         * @param customAdapt [Activity] 或 [@Fragment] 需实现 [CustomAdapt]
         */
        fun autoConvertDensityOfCustomAdapt(activity: Activity, customAdapt: CustomAdapt) {
            Preconditions.checkNotNull(customAdapt, "customAdapt == null")
            var sizeInDp = customAdapt.sizeInDp

            //如果 CustomAdapt#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
            if (sizeInDp <= 0) {
                if (customAdapt.isBaseOnWidth) {
                    sizeInDp = AutoSizeConfig.instance!!.designWidthInDp as Float
                } else {
                    sizeInDp = AutoSizeConfig.instance!!.designHeightInDp as Float
                }
            }
            autoConvertDensity(activity, sizeInDp, customAdapt.isBaseOnWidth)
        }

        /**
         * 使用外部三方库的 [Activity] 或 [@Fragment] 的自定义适配参数进行适配
         *
         * @param activity          [Activity]
         * @param externalAdaptInfo 三方库的 [Activity] 或 [@Fragment] 提供的适配参数, 需要配合 [ExternalAdaptManager.addExternalAdaptInfoOfActivity]
         */
        fun autoConvertDensityOfExternalAdaptInfo(activity: Activity, externalAdaptInfo: ExternalAdaptInfo) {
            Preconditions.checkNotNull(externalAdaptInfo, "externalAdaptInfo == null")
            var sizeInDp = externalAdaptInfo.sizeInDp

            //如果 ExternalAdaptInfo#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
            if (sizeInDp <= 0) {
                if (externalAdaptInfo.isBaseOnWidth) {
                    sizeInDp = AutoSizeConfig.instance!!.designWidthInDp as Float
                } else {
                    sizeInDp = AutoSizeConfig.instance!!.designHeightInDp as Float
                }
            }
            autoConvertDensity(activity, sizeInDp, externalAdaptInfo.isBaseOnWidth)
        }

        /**
         * 以宽度为基准进行适配
         *
         * @param activity        [Activity]
         * @param designWidthInDp 设计图的总宽度
         */
        fun autoConvertDensityBaseOnWidth(activity: Activity, designWidthInDp: Float) {
            autoConvertDensity(activity, designWidthInDp, true)
        }

        /**
         * 以高度为基准进行适配
         *
         * @param activity         [Activity]
         * @param designHeightInDp 设计图的总高度
         */
        fun autoConvertDensityBaseOnHeight(activity: Activity, designHeightInDp: Float) {
            autoConvertDensity(activity, designHeightInDp, false)
        }

        /**
         * 这里是今日头条适配方案的核心代码, 核心在于根据当前设备的实际情况做自动计算并转换 [DisplayMetrics.density]、
         * [DisplayMetrics.scaledDensity]、[DisplayMetrics.densityDpi] 这三个值, 额外增加 [DisplayMetrics.xdpi]
         * 以支持单位 `pt`、`in`、`mm`
         *
         * @param activity      [Activity]
         * @param sizeInDp      设计图上的设计尺寸, 单位 dp, 如果 {@param isBaseOnWidth} 设置为 `true`,
         * {@param sizeInDp} 则应该填写设计图的总宽度, 如果 {@param isBaseOnWidth} 设置为 `false`,
         * {@param sizeInDp} 则应该填写设计图的总高度
         * @param isBaseOnWidth 是否按照宽度进行等比例适配, `true` 为以宽度进行等比例适配, `false` 为以高度进行等比例适配
         * @see [今日头条官方适配方案](https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
         */
        fun autoConvertDensity(activity: Activity, sizeInDp: Float, isBaseOnWidth: Boolean) {
            Preconditions.checkNotNull(activity, "activity == null")

            var subunitsDesignSize = if (isBaseOnWidth)
                AutoSizeConfig.instance!!.unitsManager.getDesignWidth()
            else
                AutoSizeConfig.instance!!.unitsManager.getDesignHeight()
            subunitsDesignSize = if (subunitsDesignSize > 0) subunitsDesignSize else sizeInDp

            val screenSize = if (isBaseOnWidth)
                AutoSizeConfig.instance!!.getScreenWidth()
            else
                AutoSizeConfig.instance!!.screenHeight
            val key = (sizeInDp.toString() + "|" + subunitsDesignSize + "|" + isBaseOnWidth + "|"
                    + AutoSizeConfig.instance!!.isUseDeviceSize() + "|"
                    + AutoSizeConfig.instance!!.initScaledDensity + "|"
                    + screenSize)

            val displayMetricsInfo = mCache[key]

            var targetDensity = 0f
            var targetDensityDpi = 0
            var targetScaledDensity = 0f
            var targetXdpi = 0f
            val targetScreenWidthDp: Int
            val targetScreenHeightDp: Int

            if (displayMetricsInfo == null) {
                if (isBaseOnWidth) {
                    targetDensity = AutoSizeConfig.instance!!.getScreenWidth() * 1.0f / sizeInDp
                } else {
                    targetDensity = AutoSizeConfig.instance!!.screenHeight * 1.0f / sizeInDp
                }
                val scale = (if (AutoSizeConfig.instance!!.isExcludeFontScale())
                    1F
                else
                    AutoSizeConfig.instance!!.initScaledDensity * 1.0f / AutoSizeConfig.instance!!.initDensity).toFloat()
                targetScaledDensity = targetDensity * scale
                targetDensityDpi = (targetDensity * 160).toInt()

                targetScreenWidthDp = AutoSizeConfig.instance!!.getScreenWidth() / targetDensity as Int
                targetScreenHeightDp = AutoSizeConfig.instance!!.screenHeight / targetDensity as Int

                if (isBaseOnWidth) {
                    targetXdpi = AutoSizeConfig.instance!!.getScreenWidth() * 1.0f / subunitsDesignSize
                } else {
                    targetXdpi = AutoSizeConfig.instance!!.screenHeight * 1.0f / subunitsDesignSize
                }

                mCache[key] = DisplayMetricsInfo(
                    targetDensity,
                    targetDensityDpi,
                    targetScaledDensity,
                    targetXdpi,
                    targetScreenWidthDp,
                    targetScreenHeightDp
                )
            } else {
                targetDensity = displayMetricsInfo.density
                targetDensityDpi = displayMetricsInfo.densityDpi
                targetScaledDensity = displayMetricsInfo.scaledDensity
                targetXdpi = displayMetricsInfo.xdpi
                targetScreenWidthDp = displayMetricsInfo.screenWidthDp
                targetScreenHeightDp = displayMetricsInfo.screenHeightDp
            }

            setDensity(activity, targetDensity, targetDensityDpi, targetScaledDensity, targetXdpi)
            setScreenSizeDp(activity, targetScreenWidthDp, targetScreenHeightDp)

            LogUtils.d(
                String.format(
                    Locale.ENGLISH,
                    "The %s has been adapted! \n%s Info: isBaseOnWidth = %s, %s = %f, %s = %f, targetDensity = %f, targetScaledDensity = %f, targetDensityDpi = %d, targetXdpi = %f, targetScreenWidthDp = %d, targetScreenHeightDp = %d",
                    activity.javaClass.name,
                    activity.javaClass.simpleName,
                    isBaseOnWidth,
                    if (isBaseOnWidth)
                        "designWidthInDp"
                    else
                        "designHeightInDp",
                    sizeInDp,
                    if (isBaseOnWidth) "designWidthInSubunits" else "designHeightInSubunits",
                    subunitsDesignSize,
                    targetDensity,
                    targetScaledDensity,
                    targetDensityDpi,
                    targetXdpi,
                    targetScreenWidthDp,
                    targetScreenHeightDp
                )
            )
        }

        /**
         * 取消适配
         *
         * @param activity [Activity]
         */
        fun cancelAdapt(activity: Activity) {
            var initXdpi = AutoSizeConfig.instance!!.initXdpi
            when (AutoSizeConfig.instance!!.unitsManager.getSupportSubunits()) {
                Subunits.PT -> initXdpi /= 72f
                Subunits.MM -> initXdpi /= 25.4f
            }
            setDensity(
                activity,
                AutoSizeConfig.instance!!.initDensity,
                AutoSizeConfig.instance!!.initDensityDpi,
                AutoSizeConfig.instance!!.initScaledDensity,
                initXdpi
            )
            setScreenSizeDp(
                activity,
                AutoSizeConfig.instance!!.initScreenWidthDp,
                AutoSizeConfig.instance!!.initScreenHeightDp
            )
        }

        /**
         * 当 App 中出现多进程，并且您需要适配所有的进程，就需要在 App 初始化时调用 [.initCompatMultiProcess]
         * 建议实现自定义 [Application] 并在 [Application.onCreate] 中调用 [.initCompatMultiProcess]
         *
         * @param context [Context]
         */
        fun initCompatMultiProcess(context: Context) {
            context.contentResolver.query(
                Uri.parse("content://" + context.packageName + ".autosize-init-provider"),
                null,
                null,
                null,
                null
            )
        }

        /**
         * 给几大 [DisplayMetrics] 赋值
         *
         * @param activity      [Activity]
         * @param density       [DisplayMetrics.density]
         * @param densityDpi    [DisplayMetrics.densityDpi]
         * @param scaledDensity [DisplayMetrics.scaledDensity]
         * @param xdpi          [DisplayMetrics.xdpi]
         */
        private fun setDensity(activity: Activity, density: Float, densityDpi: Int, scaledDensity: Float, xdpi: Float) {
            //兼容 MIUI
            val activityDisplayMetricsOnMIUI = getMetricsOnMiui(activity.resources)
            val appDisplayMetricsOnMIUI = getMetricsOnMiui(AutoSizeConfig.instance!!.getApplication()!!.resources)

            if (activityDisplayMetricsOnMIUI != null) {
                setDensity(activityDisplayMetricsOnMIUI, density, densityDpi, scaledDensity, xdpi)
            } else {
                val activityDisplayMetrics = activity.resources.displayMetrics
                setDensity(activityDisplayMetrics, density, densityDpi, scaledDensity, xdpi)
            }

            if (appDisplayMetricsOnMIUI != null) {
                setDensity(appDisplayMetricsOnMIUI, density, densityDpi, scaledDensity, xdpi)
            } else {
                val appDisplayMetrics = AutoSizeConfig.instance!!.getApplication()!!.resources.displayMetrics
                setDensity(appDisplayMetrics, density, densityDpi, scaledDensity, xdpi)
            }
        }

        /**
         * 赋值
         *
         * @param displayMetrics [DisplayMetrics]
         * @param density        [DisplayMetrics.density]
         * @param densityDpi     [DisplayMetrics.densityDpi]
         * @param scaledDensity  [DisplayMetrics.scaledDensity]
         * @param xdpi           [DisplayMetrics.xdpi]
         */
        private fun setDensity(
            displayMetrics: DisplayMetrics,
            density: Float,
            densityDpi: Int,
            scaledDensity: Float,
            xdpi: Float
        ) {
            if (AutoSizeConfig.instance!!.unitsManager.isSupportDP()) {
                displayMetrics.density = density
                displayMetrics.densityDpi = densityDpi
            }
            if (AutoSizeConfig.instance!!.unitsManager.isSupportSP()) {
                displayMetrics.scaledDensity = scaledDensity
            }
            when (AutoSizeConfig.instance!!.unitsManager.getSupportSubunits()) {
                Subunits.NONE -> {
                }
                Subunits.PT -> displayMetrics.xdpi = xdpi * 72f
                Subunits.IN -> displayMetrics.xdpi = xdpi
                Subunits.MM -> displayMetrics.xdpi = xdpi * 25.4f
            }
        }

        /**
         * 给 [Configuration] 赋值
         *
         * @param activity       [Activity]
         * @param screenWidthDp  [Configuration.screenWidthDp]
         * @param screenHeightDp [Configuration.screenHeightDp]
         */
        private fun setScreenSizeDp(activity: Activity, screenWidthDp: Int, screenHeightDp: Int) {
            if (AutoSizeConfig.instance!!.unitsManager.isSupportDP() && AutoSizeConfig.instance!!.unitsManager.isSupportScreenSizeDP()) {
                val activityConfiguration = activity.resources.configuration
                setScreenSizeDp(activityConfiguration, screenWidthDp, screenHeightDp)

                val appConfiguration = AutoSizeConfig.instance!!.getApplication()!!.resources.configuration
                setScreenSizeDp(appConfiguration, screenWidthDp, screenHeightDp)
            }
        }

        /**
         * Configuration赋值
         *
         * @param configuration  [Configuration]
         * @param screenWidthDp  [Configuration.screenWidthDp]
         * @param screenHeightDp [Configuration.screenHeightDp]
         */
        private fun setScreenSizeDp(configuration: Configuration, screenWidthDp: Int, screenHeightDp: Int) {
            configuration.screenWidthDp = screenWidthDp
            configuration.screenHeightDp = screenHeightDp
        }

        /**
         * 解决 MIUI 更改框架导致的 MIUI7 + Android5.1.1 上出现的失效问题 (以及极少数基于这部分 MIUI 去掉 ART 然后置入 XPosed 的手机)
         * 来源于: https://github.com/Firedamp/Rudeness/blob/master/rudeness-sdk/src/main/java/com/bulong/rudeness/RudenessScreenHelper.java#L61:5
         *
         * @param resources [Resources]
         * @return [DisplayMetrics], 可能为 `null`
         */
        private fun getMetricsOnMiui(resources: Resources): DisplayMetrics? {
            if (AutoSizeConfig.instance!!.isMiui && AutoSizeConfig.instance!!.tmpMetricsField != null) {
                try {
                    return AutoSizeConfig.instance!!.tmpMetricsField!!.get(resources) as DisplayMetrics
                } catch (e: Exception) {
                    return null
                }

            }
            return null
        }
    }
}