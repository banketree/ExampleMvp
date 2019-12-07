package com.example.base_fun.injection.component

import android.content.Context
import com.example.base_fun.injection.module.AppMoudle
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppMoudle::class])
interface AppComponent {

    fun context(): Context

}