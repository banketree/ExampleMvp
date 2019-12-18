package me.jessyan.autosize.strategy


import android.app.Activity
import me.jessyan.autosize.core.AutoSizeConfig

/**
 * [AutoAdaptStrategy] 的包装者, 用于给 [AutoAdaptStrategy] 的实现类增加一些额外的职责
 */
class WrapperAutoAdaptStrategy(private val autoAdaptStrategy: AutoAdaptStrategy?) :
    AutoAdaptStrategy {

    override fun applyAdapt(target: Any, activity: Activity) {
        val onAdaptListener = AutoSizeConfig.instance?.getOnAdaptListener()
        onAdaptListener?.onAdaptBefore(target, activity)
        autoAdaptStrategy?.applyAdapt(target, activity)
        onAdaptListener?.onAdaptAfter(target, activity)
    }
}
