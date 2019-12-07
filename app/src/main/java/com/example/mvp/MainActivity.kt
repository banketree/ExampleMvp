package com.example.mvp

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.route.AppRoute
import com.thinkcore.activity.TAppActivity
import kotlinx.android.synthetic.main.activity_main.*


@Route(path = AppRoute.ONE_APP_MAIN)
class MainActivity : TAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test_tv.setOnClickListener {
//            AppRoute.gotoTwoDemo1Main()
//            startActivity(Intent(this@Demo1Activity, Demo1Activity::class.java))

            ARouter.getInstance().build("/two/main1").navigation()
        }
    }
}
