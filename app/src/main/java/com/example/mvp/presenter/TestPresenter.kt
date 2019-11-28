package com.example.mvp.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import com.example.baselib.mvp.presenter.IPresenter
import com.example.mvp.main.HomeActivity
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject

class TestPresenter @Inject constructor() : IPresenter {

    fun printTest() {
        Log.i("", "")
    }
}