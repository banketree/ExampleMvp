package com.example.base_lib.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.base_lib.utils.EventBusManager
import com.thinkcore.activity.TFragment

abstract class LibFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutAny = getLayoutAny()
        layoutAny?.let {
            var rootView: View?
            when (it) {
                is Int -> {
                    rootView = inflater.inflate(it, null)
                }
                is View -> {
                    rootView = it
                }
                else -> throw Exception("layout error")
            }

            initPlug()
            return rootView
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEventBus()) {
            EventBusManager.instance?.register(this)
        }
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (useEventBus()) {
            EventBusManager.instance?.unregister(this)
        }
    }

    /** 设置布局id 或 View*/
    abstract fun getLayoutAny(): Any?

    /**初始化 插件*/
    abstract fun initPlug()

    /**初始化视图*/
    abstract fun initView()

    /** 初始化数据*/
    abstract fun initData()

    /**
     * 是否使用 EventBus
     * 默认不使用
     */
    fun useEventBus(): Boolean = true
}