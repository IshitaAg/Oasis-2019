package com.dvm.appd.oasis.dbg.shared

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dvm.appd.oasis.dbg.NetworkChangeNotifier

class NetworkChangeReciver(val listener: NetworkChangeNotifier) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        listener.onNetworkStatusScahnged(NetworkChecker(context).isConnected())
    }
}
