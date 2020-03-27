package com.example.component_demo1.ui.dialog

import android.view.View
import com.example.base_fun.ui.MvpActivity
import com.example.component_demo1.R
import kotlinx.android.synthetic.main.demo1_activity_dialog.*
import kotlinx.android.synthetic.main.demo1_activity_home.*

class DialogActivity : MvpActivity<DialogPresenter>() {

    override fun injectComponent() {
        var dialogComponent = DaggerDialogComponent.builder().build()
        dialogComponent?.inject(this)
    }

    override fun getLayoutAny(): Any? = R.layout.demo1_activity_dialog

    override fun initView() {
        presenter?.init(this)

        val onClickListener = View.OnClickListener {
            when (it) {
                test1_tv -> {
                    presenter?.test1()
                }
                test2_tv -> {
                    presenter?.test2()
                }
                test3_tv -> {
                    presenter?.test3()
                }
                test4_tv -> {
                    presenter?.test4()
                }
                test5_tv -> {
                    presenter?.test5()
                }
            }
        }
        test1_tv.setOnClickListener(onClickListener)
        test2_tv.setOnClickListener(onClickListener)
        test3_tv.setOnClickListener(onClickListener)
        test4_tv.setOnClickListener(onClickListener)
        test5_tv.setOnClickListener(onClickListener)
    }

    override fun initData() {
    }

    fun initData(value: String) {
        data_tv.text = value
    }
}