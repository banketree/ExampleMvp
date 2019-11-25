package com.example.baselib.integration.lifecycle


import android.app.Activity

import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * ================================================
 * 让 [@Activity] 实现此接口,即可正常使用 [@RxLifecycle]
 * ================================================
 */
interface ActivityLifecycleable : Lifecycleable<ActivityEvent>
