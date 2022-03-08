package org.devjj.platform.nurbanhoney

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import javax.inject.Inject

@HiltAndroidApp
class AndroidApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        instance = this
        AndroidThreeTen.init(this)
    }
    companion object{
        lateinit var instance : AndroidApplication
    }
}