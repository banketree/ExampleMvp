package com.example.component_demo1.ui.dialog

import com.example.base_lib.injection.scope.PerComponentScope
import dagger.Component

@PerComponentScope
@Component
interface DialogComponent {

    fun inject(activity: DialogActivity)

}