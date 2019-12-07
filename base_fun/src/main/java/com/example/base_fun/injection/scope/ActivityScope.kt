package com.example.base_fun.injection.scope

import javax.inject.Scope
import kotlin.annotation.MustBeDocumented
import kotlin.annotation.Retention

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope