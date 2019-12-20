package com.example.component_demo1.ui.dialog

import com.example.base_fun.mvp.BasePresenter
import com.example.component_demo1.R
import om.example.base_lib.kandroid.alert
import om.example.base_lib.kandroid.toast
import timber.log.Timber
import javax.inject.Inject

class DialogPresenter @Inject constructor() : BasePresenter() {

    private var activity: DialogActivity? = null

    fun init(homeActivity: DialogActivity) {
        this.activity = homeActivity
    }

    fun test1() {
        activity?.alert {
            message("content")
            title("title")
            cancellable(false)
            negativeButton("确定") {
                dismiss()
                activity?.toast("确定")
            }
            show()
            Timber.i("" + this)
        }
    }

    fun test2() {
        activity?.alert("content", "title") {
            Timber.i("" + this)
        }
    }

    fun test3() {
        activity?.alert(R.string.abc_action_bar_home_description, R.string.abc_action_bar_up_description) {
            Timber.i("" + this)
        }
    }

    fun test4() {

    }

    fun test5() {

    }
}