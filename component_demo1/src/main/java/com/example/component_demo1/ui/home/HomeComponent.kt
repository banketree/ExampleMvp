package com.example.component_demo1.ui.home

import com.example.base_lib.injection.scope.PerComponentScope
import com.example.component_demo1.ui.demo.Demo1Activity
import dagger.Component

@PerComponentScope
@Component
interface HomeComponent {

    fun inject(activity: HomeActivity)
}