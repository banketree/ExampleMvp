package com.example.app2

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.baselib.ui.activity.BaseActivity

@Route(path = "/test2/main2")
class MainActivity : BaseActivity() {

    override fun getLayoutAny(): Any? {
       return R.layout.app2_activity_main
    }

    override fun initPlug() {
    }

    override fun initView() {
    }

    override fun initData() {
    }
}
