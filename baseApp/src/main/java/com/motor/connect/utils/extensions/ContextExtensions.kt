package com.motor.connect.utils.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.support.annotation.RequiresPermission

@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)

fun Context.isConnected(): Boolean {
    val activeNetworkInfo = (getSystemService(Context.CONNECTIVITY_SERVICE) as
            ConnectivityManager).activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


