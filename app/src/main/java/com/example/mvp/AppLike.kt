package com.example.mvp

import com.mvp.provider.router.component.IApplicationLike
import com.mvp.provider.router.component.Router
import com.mvp.provider.router.service.AppService

class AppLike : IApplicationLike {

    var router = Router.instance

    override fun registered() {
        router.addService(AppService::class.java.simpleName, AppServiceImel())
    }

    override fun unregistered() {
        router.removeService(AppService::class.java.simpleName)
    }
}