package com.example.mvp.jetpack.livedata

import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import timber.log.Timber


@Suppress("DEPRECATION")
class NetworkLiveData(context: Context) : LiveData<NetworkInfo>() {

    private val mContext: Context = context.applicationContext
    private val mNetworkReceiver: NetworkReceiver
    private val mIntentFilter: IntentFilter

    init {
        mNetworkReceiver = NetworkReceiver()
        mIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    override fun onActive() {
        super.onActive()
        Timber.d( "onActive:")
        mContext.registerReceiver(mNetworkReceiver, mIntentFilter)
    }

    override fun onInactive() {
        super.onInactive()
        Timber.d( "onInactive: ")
        mContext.unregisterReceiver(mNetworkReceiver)
    }

    private class NetworkReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val manager = context
                .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = manager.activeNetworkInfo
            getInstance(context).value = activeNetwork

        }
    }

    companion object {
        internal var mNetworkLiveData: NetworkLiveData? = null

        fun getInstance(context: Context): NetworkLiveData {
            if (mNetworkLiveData == null) {
                mNetworkLiveData = NetworkLiveData(context)
            }
            return mNetworkLiveData as NetworkLiveData
        }
    }
}