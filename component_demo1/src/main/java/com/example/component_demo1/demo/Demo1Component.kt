package com.example.component_demo1.demo

import com.example.base_fun.injection.scope.PerComponentScope
import dagger.Component

@PerComponentScope
@Component(modules = [Demo1Moudle::class])
interface Demo1Component {

    fun inject(activity: Demo1Activity)
}