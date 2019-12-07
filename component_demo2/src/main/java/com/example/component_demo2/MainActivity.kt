package com.example.component_demo2

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.route.AppRoute
import com.thinkcore.activity.TAppActivity
import kotlinx.android.synthetic.main.demo2_activity_main.*

@Route(path = AppRoute.TWO_DEMO2_MAIN)
class MainActivity : TAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo2_activity_main)

        test_tv.setOnClickListener {
            AppRoute.gotoTwoDemo3Main()
        }
    }
}
