package com.josus.notesapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.getSystemService

class ConnectionManager {

    fun checkConnectivity(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetWork :NetworkInfo? = connectivityManager.activeNetworkInfo

        return if (activeNetWork?.isConnected != null){
            activeNetWork.isConnected
        }
        else{
            false
        }
    }
}