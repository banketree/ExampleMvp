package com.example.mvp.main.mvp

import com.example.mvp.presenter.WordPresenter
import dagger.Module
import dagger.Provides

@Module
class HomeModule(private val view: HomeContract.View) {
    private var wordPresenter: WordPresenter? = null

    @Provides
    fun provideView(): HomeContract.View {
        return this.view
    }

    @Provides
    fun provideWordPresenter(): WordPresenter {
        if (wordPresenter == null) wordPresenter = WordPresenter()
        return wordPresenter!!
    }
}