package com.benyq.sodaplanet.audio

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import com.benyq.sodaplanet.base.ext.fromM

/**
 *
 * @author benyq
 * @date 2022/10/28
 * @email 1520063035@qq.com
 *
 */

fun Context.isWifiConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (fromM()) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun Context.getWifiSSD(): String {
    if (isWifiConnected()) {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return wifiInfo.ssid
    }
    return ""
}

fun Context.getConnectWifiIp(): String {
    if (isWifiConnected()) {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        return if (ipAddress == 0) {
            ""
        } else ((ipAddress and 0xff).toString() + "." + (ipAddress shr 8 and 0xff) + "."
                + (ipAddress shr 16 and 0xff) + "." + (ipAddress shr 24 and 0xff))
    }
    return ""
}