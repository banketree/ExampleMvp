package com.example.baselib.injection.module

import android.content.Context
import com.example.baselib.common.MvpApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppMoudle(private val context: MvpApplication){

    @Singleton
    @Provides
    fun provideContext(): Context {
        return this.context
    }
}