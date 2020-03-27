package com.example.base_fun.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base_lib.ui.LibFragment
import me.jessyan.autosize.AutoSize

abstract class BaseFragment : LibFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        AutoSize.autoConvertDensityOfGlobal(activity!!)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}