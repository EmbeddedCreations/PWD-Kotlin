package com.example.pwd_app.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Handler
import android.os.Looper

class NetworkStatusUtility(private val context: Context) {
    private val connectivityManager: ConnectivityManager?
    private val mainHandler: Handler
    private var networkCallback: NetworkCallback? = null

    init {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mainHandler = Handler(Looper.getMainLooper())
    }

    val isNetworkAvailable: Boolean
        get() {
            if (connectivityManager != null) {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
            return false
        }

    fun startMonitoringNetworkStatus(listener: NetworkStatusListener) {
        if (connectivityManager == null) {
            return
        }
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                mainHandler.post { listener.onNetworkAvailable() }
            }

            override fun onLost(network: Network) {
                mainHandler.post { listener.onNetworkLost() }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback as NetworkCallback)
    }

    fun stopMonitoringNetworkStatus() {
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback!!)
        }
    }

    interface NetworkStatusListener {
        fun onNetworkAvailable()
        fun onNetworkLost()
    }
}