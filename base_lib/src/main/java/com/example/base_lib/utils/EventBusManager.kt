package com.example.base_lib.utils


import org.greenrobot.eventbus.EventBus

import java.lang.reflect.Method

/**
 * EventBus 的管理类
 */
class EventBusManager private constructor() {

    /**
     * 注册订阅者
     */
    fun register(subscriber: Any) {
        if (!haveAnnotation(subscriber)) return
        EventBus.getDefault().register(subscriber)
    }

    /**
     * 注销订阅者
     */
    fun unregister(subscriber: Any) {
        if (!haveAnnotation(subscriber)) return
        EventBus.getDefault().unregister(subscriber)
    }

    /**
     * 发送事件
     */
    fun post(event: Any) {
        EventBus.getDefault().post(event)
    }

    /**
     * 发送黏性事件
     */
    fun postSticky(event: Any) {
        EventBus.getDefault().postSticky(event)
    }

    /**
     * 注销黏性事件
     */
    fun <T> removeStickyEvent(eventType: Class<T>): T {
        return EventBus.getDefault().removeStickyEvent(eventType)
    }

    /**
     * 清除订阅者和事件的缓存
     */
    fun clear() {
        EventBus.clearCaches()
    }

    /**
     * 要求注册/注解之前, 订阅者必须含有一个或以上声明
     * 否则会报错, 所以如果要想完成在基类中自动注册, 避免报错就要先检查是否符合注册资格
     */
    private fun haveAnnotation(subscriber: Any): Boolean {
        var skipSuperClasses = false
        var clazz: Class<*>? = subscriber.javaClass
        //查找类中符合注册要求的方法, 直到Object类
        while (clazz != null && !isSystemCalss(clazz.name) && !skipSuperClasses) {
            var allMethods: Array<Method>
            try {
                allMethods = clazz.declaredMethods
            } catch (th: Throwable) {
                try {
                    allMethods = clazz.methods
                } catch (th2: Throwable) {
                    continue
                } finally {
                    skipSuperClasses = true
                }
            }

            for (i in allMethods.indices) {
                val method = allMethods[i]
                val parameterTypes = method.parameterTypes
                //查看该方法是否含有 Subscribe 注解
                if (method.isAnnotationPresent(org.greenrobot.eventbus.Subscribe::class.java) && parameterTypes.size == 1) {
                    return true
                }
            } //end for
            //获取父类, 以继续查找父类中符合要求的方法
            clazz = clazz.superclass
        }
        return false
    }

    private fun isSystemCalss(name: String): Boolean {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")
    }

    companion object {
        @Volatile
        private var sInstance: EventBusManager? = null

        val instance: EventBusManager?
            get() {
                if (sInstance == null) {
                    synchronized(EventBusManager::class.java) {
                        if (sInstance == null) {
                            sInstance = EventBusManager()
                        }
                    }
                }
                return sInstance
            }
    }
}
