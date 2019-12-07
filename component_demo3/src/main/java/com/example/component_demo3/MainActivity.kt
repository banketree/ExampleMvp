package com.example.component_demo3

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.route.AppRoute
import com.thinkcore.activity.TAppActivity
import kotlinx.android.synthetic.main.demo3_activity_main.*

@Route(path = AppRoute.TWO_DEMO3_MAIN)
class MainActivity : TAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo3_activity_main)

        test_tv.setOnClickListener {
            AppRoute.gotoOneAppMain()
        }
    }
}
