package com.example.mvp.main.mvp

import com.example.baselib.injection.component.ActivityComponent
import com.example.baselib.injection.scope.PerComponentScope
import com.example.mvp.main.MainActivity
import dagger.Component

@PerComponentScope
@Component(dependencies = [ActivityComponent::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}