package com.example.component_demo1

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.route.AppRoute
import com.thinkcore.activity.TAppActivity
import kotlinx.android.synthetic.main.demo1_activity_main.*

@Route(path = AppRoute.TWO_DEMO1_MAIN)
class Demo1Activity : TAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo1_activity_main)

        test_tv.setOnClickListener {
            AppRoute.gotoTwoDemo2Main()
        }
    }
}
