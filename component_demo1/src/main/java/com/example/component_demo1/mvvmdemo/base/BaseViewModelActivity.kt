package com.example.component_demo1.mvvmdemo.base
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.base_lib.ui.BaseActivity

abstract class BaseViewModelActivity<VM: BaseViewModel> : BaseActivity() {

    protected lateinit var viewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        initVM()
        super.onCreate(savedInstanceState)
    }

    private fun initVM() {
        providerVMClass()?.let {
            viewModel = ViewModelProviders.of(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    open fun providerVMClass(): Class<VM>? = null

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}