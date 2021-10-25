package org.devjj.platform.nurbanhoney

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import javax.inject.Inject

@HiltAndroidApp
class AndroidApplication : Application(){

    @Inject
    lateinit var prefs : SharedPreferences
    @Inject
    lateinit var navigator: Navigator
    override fun onCreate() {
        super.onCreate()

        Log.d("prefs_check__", prefs.all.toString())
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
    }
}