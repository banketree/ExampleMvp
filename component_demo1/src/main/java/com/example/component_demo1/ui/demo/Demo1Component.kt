package com.example.component_demo1.ui.demo

import com.example.base_lib.injection.scope.PerComponentScope
import dagger.Component

@PerComponentScope
@Component(modules = [Demo1Moudle::class])
interface Demo1Component {

    fun inject(activity: Demo1Activity)
}