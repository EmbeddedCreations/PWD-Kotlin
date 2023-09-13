package com.example.pwd_app.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

@Suppress("DEPRECATION")
class NetworkUtil {
    companion object{
        fun isInternetAvailable(context: Context): Boolean{
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return this.getNetworkCapabilities(this.activeNetwork)?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) ?: false
            }else{
                (
                return this.activeNetworkInfo?.isConnected?: false)
            }
            }

        }
    }
}