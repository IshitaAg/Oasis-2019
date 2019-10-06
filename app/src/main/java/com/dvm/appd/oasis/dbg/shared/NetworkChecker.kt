package com.dvm.appd.oasis.dbg.shared

import android.content.Context
import android.net.ConnectivityManager

class NetworkChecker(private val context: Context) {
    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}