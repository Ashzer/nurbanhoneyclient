package org.devjj.platform.nurbanhoney.core.extension

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager

val Context.connectivityManager: ConnectivityManager
    get() =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
