package com.example.base_fun.injection.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppMoudle(private val context: Application){

    @Singleton
    @Provides
    fun provideContext(): Context {
        return this.context
    }
}