package org.devjj.platform.nurbanhoney

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.kakao.sdk.common.KakaoSdk

import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AndroidApplication : Application(){

    @Inject
    lateinit var prefs : SharedPreferences

    override fun onCreate() {
        super.onCreate()

        //Log.d("prefs_check__", prefs.all.toString())

        KakaoSdk.init(this,"8afc185b39976c8da64f53954aff69ff")


    }
}