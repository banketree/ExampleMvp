package me.jessyan.autosize.external


import android.app.Activity

import java.util.ArrayList
import java.util.HashMap

import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.utils.Preconditions

/**
 * 管理三方库的适配信息和状态, 通过 [AutoSizeConfig.getExternalAdaptManager] 获取, 切勿自己 new
 * AndroidAutoSize 通过实现接口的方式来让每个 [Activity] 都具有自定义适配参数的功能, 从而让每个 [Activity] 都可以自定义适配效果
 * 但通过远程依赖的三方库并不能修改源码, 所以也不能让三方库的 [Activity] 实现接口, 实现接口的方式就显得无能为力
 */
class ExternalAdaptManager {
    private var mCancelAdaptList: MutableList<String>? = null
    private var mExternalAdaptInfos: MutableMap<String, ExternalAdaptInfo>? = null
    private var isRun: Boolean = false

    /**
     * 将不需要适配的第三方库 [Activity] 添加进来 (但不局限于三方库), 即可让该 [Activity] 的适配效果失效
     *
     *
     * 支持链式调用, 如:
     * [ExternalAdaptManager.addCancelAdaptOfActivity]
     *
     * @param targetClass [Activity] class, [@Fragment] class
     */
    @Synchronized
    fun addCancelAdaptOfActivity(targetClass: Class<*>): ExternalAdaptManager {
        Preconditions.checkNotNull(targetClass, "targetClass == null")
        if (!isRun) {
            isRun = true
        }
        if (mCancelAdaptList == null) {
            mCancelAdaptList = ArrayList()
        }
        targetClass.canonicalName?.let { mCancelAdaptList!!.add(it) }
        return this
    }

    /**
     * 将需要提供自定义适配参数的三方库 [Activity] 添加进来 (但不局限于三方库), 即可让该 [Activity] 根据自己提供的适配参数进行适配
     * 默认的全局适配参数不能满足您时可以使用此方法
     *
     * @param targetClass [Activity] class, [@Fragment] class
     * @param info        [ExternalAdaptInfo] 适配参数
     */
    @Synchronized
    fun addExternalAdaptInfoOfActivity(targetClass: Class<*>, info: ExternalAdaptInfo): ExternalAdaptManager {
        Preconditions.checkNotNull(targetClass, "targetClass == null")
        if (!isRun) {
            isRun = true
        }
        if (mExternalAdaptInfos == null) {
            mExternalAdaptInfos = HashMap(16)
        }
        mExternalAdaptInfos!![targetClass.canonicalName!!] = info
        return this
    }

    /**
     * 这个 [Activity] 是否存在在取消适配的列表中, 如果在, 则该 [Activity] 适配失效
     *
     * @param targetClass [Activity] class, [@Fragment] class
     * @return `true` 为存在, `false` 为不存在
     */
    @Synchronized
    fun isCancelAdapt(targetClass: Class<*>): Boolean {
        Preconditions.checkNotNull(targetClass, "targetClass == null")
        return if (mCancelAdaptList == null) {
            false
        } else mCancelAdaptList!!.contains(targetClass.canonicalName)
    }

    /**
     * 这个 [Activity] 是否提供有自定义的适配参数, 如果有则使用此适配参数进行适配
     *
     * @param targetClass [Activity] class, [@Fragment] class
     * @return 如果返回 `null` 则说明该 [Activity] 没有提供自定义的适配参数
     */
    @Synchronized
    fun getExternalAdaptInfoOfActivity(targetClass: Class<*>): ExternalAdaptInfo? {
        Preconditions.checkNotNull(targetClass, "targetClass == null")
        return if (mExternalAdaptInfos == null) {
            null
        } else mExternalAdaptInfos!![targetClass.canonicalName]
    }

    /**
     * 此管理器是否已经启动
     *
     * @return `true` 为已经启动, `false` 为没有启动
     */
    fun isRun(): Boolean {
        return isRun
    }

    /**
     * 设置管理器的运行状态
     *
     * @param run `true` 为让管理器启动运行, `false` 为让管理器停止运行
     */
    fun setRun(run: Boolean): ExternalAdaptManager {
        isRun = run
        return this
    }
}
