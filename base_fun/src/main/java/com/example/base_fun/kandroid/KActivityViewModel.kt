package com.example.base_fun.kandroid

import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

class ToastViewModel : ViewModel() {

    val toastData: MutableLiveData<String> = MutableLiveData()

    fun show(text: String) {
        if (TextUtils.isEmpty(text)) return
        toastData.value = text
    }
}

//val AppCompatActivity.toastViewModel: ToastViewModel
//    get() {
//        return ViewModelProviders.of(this).get(ToastViewModel::class.java)
//    }
//
//inline fun AppCompatActivity.toast(text: CharSequence): Toast =
//    Toast.makeText(this, text, Toast.LENGTH_SHORT).apply { show() }
//
//inline fun AppCompatActivity.longToast(text: CharSequence): Toast =
//    Toast.makeText(this, text, Toast.LENGTH_LONG).apply { show() }
//
//inline fun AppCompatActivity.toast(@StringRes resId: Int): Toast =
//    Toast.makeText(this, resId, Toast.LENGTH_SHORT).apply { show() }
//
//inline fun AppCompatActivity.longToast(@StringRes resId: Int): Toast =
//    Toast.makeText(this, resId, Toast.LENGTH_LONG).apply { show() }