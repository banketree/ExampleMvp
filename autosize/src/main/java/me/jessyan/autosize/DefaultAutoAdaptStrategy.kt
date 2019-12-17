package me.jessyan.autosize

import android.app.Activity
import android.app.Application
import me.jessyan.autosize.external.ExternalAdaptInfo
import me.jessyan.autosize.internal.CancelAdapt
import me.jessyan.autosize.internal.CustomAdapt
import me.jessyan.autosize.utils.LogUtils

import java.util.Locale

/**
 * 屏幕适配逻辑策略默认实现类, 可通过 [AutoSizeConfig.init]
 * 和 [AutoSizeConfig.setAutoAdaptStrategy] 切换策略
 */
class DefaultAutoAdaptStrategy : AutoAdaptStrategy {
    override fun applyAdapt(target: Any, activity: Activity) {

        //检查是否开启了外部三方库的适配模式, 只要不主动调用 ExternalAdaptManager 的方法, 下面的代码就不会执行
        if (AutoSizeConfig.instance!!.externalAdaptManager.isRun()) {
            if (AutoSizeConfig.instance!!.externalAdaptManager.isCancelAdapt(target.javaClass)) {
                LogUtils.w(String.format(Locale.ENGLISH, "%s canceled the adaptation!", target.javaClass.name))
                AutoSize.cancelAdapt(activity)
                return
            } else {
                val info = AutoSizeConfig.instance!!.externalAdaptManager
                    .getExternalAdaptInfoOfActivity(target.javaClass)
                if (info != null) {
                    LogUtils.d(
                        String.format(
                            Locale.ENGLISH,
                            "%s used %s for adaptation!",
                            target.javaClass.name,
                            ExternalAdaptInfo::class.java!!.name
                        )
                    )
                    AutoSize.autoConvertDensityOfExternalAdaptInfo(activity, info)
                    return
                }
            }
        }

        //如果 target 实现 CancelAdapt 接口表示放弃适配, 所有的适配效果都将失效
        if (target is CancelAdapt) {
            LogUtils.w(String.format(Locale.ENGLISH, "%s canceled the adaptation!", target.javaClass.name))
            AutoSize.cancelAdapt(activity)
            return
        }

        //如果 target 实现 CustomAdapt 接口表示该 target 想自定义一些用于适配的参数, 从而改变最终的适配效果
        if (target is CustomAdapt) {
            LogUtils.d(
                String.format(
                    Locale.ENGLISH,
                    "%s implemented by %s!",
                    target.javaClass.name,
                    CustomAdapt::class.java!!.name
                )
            )
            AutoSize.autoConvertDensityOfCustomAdapt(activity, target as CustomAdapt)
        } else {
            LogUtils.d(String.format(Locale.ENGLISH, "%s used the global configuration.", target.javaClass.name))
            AutoSize.autoConvertDensityOfGlobal(activity)
        }
    }
}
