package com.example.mvp.main.mvp

import dagger.Module
import dagger.Provides

@Module
class MainModule(private val view: MainContract.View) {

    @Provides
    fun provideView(): MainContract.View {
        return this.view
    }

}