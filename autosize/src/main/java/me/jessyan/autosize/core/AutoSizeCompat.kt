package me.jessyan.autosize.core

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import me.jessyan.autosize.bean.DisplayMetricsInfo
import me.jessyan.autosize.bean.ExternalAdaptInfo
import me.jessyan.autosize.internal.CustomAdapt
import me.jessyan.autosize.unit.Subunits
import me.jessyan.autosize.utils.Preconditions
import java.util.concurrent.ConcurrentHashMap

/**
 * 当遇到本来适配正常的布局突然出现适配失效，适配异常等问题, 重写当前 [Activity] 的 [Activity.getResources] 并调用
 * [AutoSizeCompat] 的对应方法即可解决问题
 */
object AutoSizeCompat {

    private val cache = ConcurrentHashMap<String, DisplayMetricsInfo>()

    /**
     * 使用 AndroidAutoSize 初始化时设置的默认适配参数进行适配 (AndroidManifest 的 Meta 属性)
     *
     * @param resources [Resources]
     */
    fun autoConvertDensityOfGlobal(resources: Resources) {
        AutoSizeConfig.instance?.apply {
            if (isBaseOnWidth()) {
                autoConvertDensityBaseOnWidth(
                    resources,
                    designHeightInDp.toFloat()
                )
            } else {
                autoConvertDensityBaseOnHeight(
                    resources,
                    designHeightInDp.toFloat()
                )
            }
        }
    }

    /**
     * 使用 [Activity] 或 [@Fragment] 的自定义参数进行适配
     *
     * @param resources   [Resources]
     * @param customAdapt [Activity] 或 [@Fragment] 需实现 [@CustomAdapt]
     */
    fun autoConvertDensityOfCustomAdapt(resources: Resources, customAdapt: CustomAdapt) {
        Preconditions.checkNotNull(customAdapt, "customAdapt == null")
        var sizeInDp = customAdapt.sizeInDp

        //如果 CustomAdapt#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
        if (sizeInDp <= 0) {
            sizeInDp = if (customAdapt.isBaseOnWidth) {
                AutoSizeConfig.instance!!.designWidthInDp.toFloat()
            } else {
                AutoSizeConfig.instance!!.designHeightInDp.toFloat()
            }
        }
        autoConvertDensity(
            resources,
            sizeInDp,
            customAdapt.isBaseOnWidth
        )
    }

    /**
     * 使用外部三方库的 [Activity] 或 [@Fragment] 的自定义适配参数进行适配
     *
     * @param resources         [Resources]
     * @param externalAdaptInfo 三方库的 [Activity] 或 [@sFragment] 提供的适配参数, 需要配合 [ExternalAdaptManager.addExternalAdaptInfoOfActivity]
     */
    fun autoConvertDensityOfExternalAdaptInfo(resources: Resources, externalAdaptInfo: ExternalAdaptInfo) {
        Preconditions.checkNotNull(externalAdaptInfo, "externalAdaptInfo == null")
        var sizeInDp = externalAdaptInfo.sizeInDp

        //如果 ExternalAdaptInfo#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
        if (sizeInDp <= 0) {
            sizeInDp = if (externalAdaptInfo.isBaseOnWidth) {
                AutoSizeConfig.instance!!.designWidthInDp.toFloat()
            } else {
                AutoSizeConfig.instance!!.designHeightInDp.toFloat()
            }
        }
        autoConvertDensity(
            resources,
            sizeInDp,
            externalAdaptInfo.isBaseOnWidth
        )
    }

    /**
     * 以宽度为基准进行适配
     *
     * @param resources       [Resources]
     * @param designWidthInDp 设计图的总宽度
     */
    fun autoConvertDensityBaseOnWidth(resources: Resources, designWidthInDp: Float) {
        autoConvertDensity(resources, designWidthInDp, true)
    }

    /**
     * 以高度为基准进行适配
     *
     * @param resources        [Resources]
     * @param designHeightInDp 设计图的总高度
     */
    fun autoConvertDensityBaseOnHeight(resources: Resources, designHeightInDp: Float) {
        autoConvertDensity(resources, designHeightInDp, false)
    }

    /**
     * 这里是今日头条适配方案的核心代码, 核心在于根据当前设备的实际情况做自动计算并转换 [DisplayMetrics.density]、
     * [DisplayMetrics.scaledDensity]、[DisplayMetrics.densityDpi] 这三个值, 额外增加 [DisplayMetrics.xdpi]
     * 以支持单位 `pt`、`in`、`mm`
     *
     * @param resources     [Resources]
     * @param sizeInDp      设计图上的设计尺寸, 单位 dp, 如果 {@param isBaseOnWidth} 设置为 `true`,
     * {@param sizeInDp} 则应该填写设计图的总宽度, 如果 {@param isBaseOnWidth} 设置为 `false`,
     * {@param sizeInDp} 则应该填写设计图的总高度
     * @param isBaseOnWidth 是否按照宽度进行等比例适配, `true` 为以宽度进行等比例适配, `false` 为以高度进行等比例适配
     * @see [今日头条官方适配方案](https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
     */
    fun autoConvertDensity(resources: Resources, sizeInDp: Float, isBaseOnWidth: Boolean) {
        Preconditions.checkNotNull(resources, "resources == null")

        var subunitsDesignSize = if (isBaseOnWidth)
            AutoSizeConfig.instance!!.unitsManager.getDesignWidth()
        else
            AutoSizeConfig.instance!!.unitsManager.getDesignHeight()
        subunitsDesignSize = if (subunitsDesignSize > 0) subunitsDesignSize else sizeInDp

        val screenSize = if (isBaseOnWidth)
            AutoSizeConfig.instance!!.getScreenWidth()
        else
            AutoSizeConfig.instance!!.getScreenHeight()
        val key = (sizeInDp.toString() + "|" + subunitsDesignSize + "|" + isBaseOnWidth + "|"
                + AutoSizeConfig.instance!!.isUseDeviceSize() + "|"
                + AutoSizeConfig.instance!!.initScaledDensity + "|"
                + screenSize)

        val displayMetricsInfo = cache[key]

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
                targetDensity = AutoSizeConfig.instance!!.getScreenHeight() * 1.0f / sizeInDp
            }
            val scale = (if (AutoSizeConfig.instance!!.isExcludeFontScale())
                1F
            else
                AutoSizeConfig.instance!!.initScaledDensity * 1.0f / AutoSizeConfig.instance!!.initDensity)
            targetScaledDensity = targetDensity * scale
            targetDensityDpi = (targetDensity * 160).toInt()

            targetScreenWidthDp = (AutoSizeConfig.instance!!.getScreenWidth() / targetDensity).toInt()
            targetScreenHeightDp = (AutoSizeConfig.instance!!.getScreenHeight() / targetDensity).toInt()

            targetXdpi = if (isBaseOnWidth) {
                AutoSizeConfig.instance!!.getScreenWidth() * 1.0f / subunitsDesignSize
            } else {
                AutoSizeConfig.instance!!.getScreenHeight() * 1.0f / subunitsDesignSize
            }

            cache[key] = DisplayMetricsInfo(
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

        setDensity(
            resources,
            targetDensity,
            targetDensityDpi,
            targetScaledDensity,
            targetXdpi
        )
        setScreenSizeDp(
            resources,
            targetScreenWidthDp,
            targetScreenHeightDp
        )
    }

    /**
     * 取消适配
     *
     * @param resources [Resources]
     */
    fun cancelAdapt(resources: Resources) {
        AutoSizeConfig.instance?.apply {
            var initXdpi = this.initXdpi
            when (unitsManager.getSupportSubunits()) {
                Subunits.PT -> initXdpi /= 72f
                Subunits.MM -> initXdpi /= 25.4f
            }
            setDensity(
                resources,
                initDensity,
                initDensityDpi,
                initScaledDensity,
                initXdpi
            )
            setScreenSizeDp(
                resources,
                initScreenWidthDp,
                initScreenHeightDp
            )
        }
    }

    /**
     * 给几大 [DisplayMetrics] 赋值
     *
     * @param resources     [Resources]
     * @param density       [DisplayMetrics.density]
     * @param densityDpi    [DisplayMetrics.densityDpi]
     * @param scaledDensity [DisplayMetrics.scaledDensity]
     * @param xdpi          [DisplayMetrics.xdpi]
     */
    private fun setDensity(
        resources: Resources,
        density: Float,
        densityDpi: Int,
        scaledDensity: Float,
        xdpi: Float
    ) {
        //兼容 MIUI
        val activityDisplayMetricsOnMIUI =
            getMetricsOnMiui(resources)
        val appDisplayMetricsOnMIUI =
            getMetricsOnMiui(AutoSizeConfig.instance!!.application.resources)

        if (activityDisplayMetricsOnMIUI != null) {
            setDensity(
                activityDisplayMetricsOnMIUI,
                density,
                densityDpi,
                scaledDensity,
                xdpi
            )
        } else {
            val activityDisplayMetrics = resources.displayMetrics
            setDensity(
                activityDisplayMetrics,
                density,
                densityDpi,
                scaledDensity,
                xdpi
            )
        }

        if (appDisplayMetricsOnMIUI != null) {
            setDensity(
                appDisplayMetricsOnMIUI,
                density,
                densityDpi,
                scaledDensity,
                xdpi
            )
        } else {
            val appDisplayMetrics = AutoSizeConfig.instance!!.application.resources.displayMetrics
            setDensity(
                appDisplayMetrics,
                density,
                densityDpi,
                scaledDensity,
                xdpi
            )
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
        AutoSizeConfig.instance?.apply {
            if (unitsManager.isSupportDP()) {
                displayMetrics.density = density
                displayMetrics.densityDpi = densityDpi
            }
            if (unitsManager.isSupportSP()) {
                displayMetrics.scaledDensity = scaledDensity
            }
            when (unitsManager.getSupportSubunits()) {
                Subunits.NONE -> {
                }
                Subunits.PT -> displayMetrics.xdpi = xdpi * 72f
                Subunits.IN -> displayMetrics.xdpi = xdpi
                Subunits.MM -> displayMetrics.xdpi = xdpi * 25.4f
            }
        }
    }

    /**
     * 给 [Configuration] 赋值
     *
     * @param resources      [Resources]
     * @param screenWidthDp  [Configuration.screenWidthDp]
     * @param screenHeightDp [Configuration.screenHeightDp]
     */
    private fun setScreenSizeDp(resources: Resources, screenWidthDp: Int, screenHeightDp: Int) {
        AutoSizeConfig.instance?.apply {
            if (unitsManager.isSupportDP() && unitsManager.isSupportScreenSizeDP()) {
                val activityConfiguration = resources.configuration
                setScreenSizeDp(
                    activityConfiguration,
                    screenWidthDp,
                    screenHeightDp
                )

                val appConfiguration = application.resources.configuration
                setScreenSizeDp(
                    appConfiguration,
                    screenWidthDp,
                    screenHeightDp
                )
            }
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
            return try {
                AutoSizeConfig.instance!!.tmpMetricsField!!.get(resources) as DisplayMetrics
            } catch (e: Exception) {
                null
            }

        }
        return null
    }
}
