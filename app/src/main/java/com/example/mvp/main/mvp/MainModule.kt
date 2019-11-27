package com.example.mvp.main.mvp

import com.example.mvp.presenter.WordPresenter
import dagger.Module
import dagger.Provides

@Module
class MainModule(private val view: MainContract.View) {
    private var wordPresenter: WordPresenter? = null

    @Provides
    fun provideView(): MainContract.View {
        return this.view
    }

    @Provides
    fun provideWordPresenter(): WordPresenter {
        if (wordPresenter == null) wordPresenter = WordPresenter()
        return wordPresenter!!
    }
}