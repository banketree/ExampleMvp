package com.example.component_demo2.demo

import com.example.base_fun.injection.component.ActivityComponent
import com.example.base_lib.injection.scope.PerComponentScope
import dagger.Component

@PerComponentScope
@Component(dependencies = [ActivityComponent::class])
interface Demo2Component {
    fun inject(activity: Demo2Activity)
}