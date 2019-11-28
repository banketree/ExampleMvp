package com.example.mvp

import android.util.Log
import com.mvp.provider.router.service.AppService
import javax.inject.Inject

class AppServiceImel @Inject constructor() : AppService {
    override fun getApp(): Any {
        Log.i("", "getApp")
        Log.i("", "getApp")
        Log.i("", "getApp")
        Log.i("", "getApp")
        return 111
    }
}