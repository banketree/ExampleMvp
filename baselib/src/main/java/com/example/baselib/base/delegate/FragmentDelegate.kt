package com.example.baselib.base.delegate


import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * ================================================
 * 代理类,用于框架内部在每个Fragment 的对应生命周期中插入需要的逻辑
 * ================================================
 */
interface FragmentDelegate {

    /**
     * Return true if the fragment is currently added to its activity.
     */
    val isAdded: Boolean

    fun onAttach(context: Context)

    fun onCreate(savedInstanceState: Bundle?)

    fun onCreateView(view: View?, savedInstanceState: Bundle?)

    fun onActivityCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroyView()

    fun onDestroy()

    fun onDetach()

    companion object {
        val FRAGMENT_DELEGATE = "FRAGMENT_DELEGATE"
    }
}