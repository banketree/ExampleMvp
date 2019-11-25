package com.example.baselib.integration.lifecycle


import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * ================================================
 * 让 [@Fragment] 实现此接口,即可正常使用 [@RxLifecycle]
 * ================================================
 */
interface FragmentLifecycleable : Lifecycleable<FragmentEvent>
