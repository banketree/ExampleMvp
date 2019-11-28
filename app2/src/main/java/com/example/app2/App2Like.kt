package com.example.app2

import com.mvp.provider.router.component.IApplicationLike
import com.mvp.provider.router.component.Router
import com.mvp.provider.router.service.App2Service

class App2Like : IApplicationLike {

    var router = Router.instance

    override fun registered() {
        router.addService(App2Service::class.java.simpleName, App2ServiceImel())
    }

    override fun unregistered() {
        router.removeService(App2Service::class.java.simpleName)
    }
}