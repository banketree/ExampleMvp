package com.example.base_fun.dialog

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import com.example.base_fun.R


class LoadingDialog(val context: Context) {
    private var alertDialog: AlertDialog? = null

    fun show() {
        if (context !is Activity) return

        alertDialog = AlertDialog.Builder(context).create()
        alertDialog?.apply {
            window!!.setBackgroundDrawable(ColorDrawable())
            setCancelable(false)
            setOnKeyListener { dialog, keyCode, event ->
                keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK
            }
            show()
            setContentView(R.layout.loading_dialog)
            setCanceledOnTouchOutside(false)
        }
    }

    fun hide() {
        alertDialog?.apply {
            dismiss()
        }
    }

    fun isShowing(): Boolean = alertDialog != null && alertDialog!!.isShowing
}