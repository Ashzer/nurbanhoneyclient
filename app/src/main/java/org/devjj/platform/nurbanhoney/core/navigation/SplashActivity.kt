package org.devjj.platform.nurbanhoney.core.navigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.InputMethodManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.devjj.platform.nurbanhoney.features.network.VersionCheckService
import org.devjj.platform.nurbanhoney.features.network.UpdateManager
import java.lang.Exception
import javax.inject.Inject
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SplashActivity
@Inject constructor() : AppCompatActivity() {

    private val SPLASH_DISPLAY_TIME = 3000L
    @Inject
    internal lateinit var navigator: Navigator
    @Inject
    internal lateinit var versionCheckService: VersionCheckService
    @Inject
    internal lateinit var updateManager: UpdateManager
    @Inject
    lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed( {
            runBlocking { navigator.showMain(this@SplashActivity) }
            finish()
        },SPLASH_DISPLAY_TIME)


        CoroutineScope(Dispatchers.IO).launch {
            //Log.d("version_check__", versionCheckService.appVersion("nurbanhoney").execute().body().toString())

            var version = versionCheckService.appVersion("nurbanhoney").execute().body().also {
                Log.d("type_check__", "${it?.javaClass}")
                Log.d("type_check__", "${it.toString()}")
            }

                try {
                    Log.d("version_check__", version?.result?.version)
                    when(version?.result?.isUpdate){
                        // 어플이 업로드 되기 전에 테스트 불가능?
                        true -> updateManager.update(activity = this@SplashActivity)
                        //false ->updateManager.update(activity = this@SplashActivity)
                    }
                }catch (e : Exception){
                    //val error = Gson().fromJson(json.get("appversion_result").asJsonObject, Error::class.java)
                    Log.d("version_check__", "error occurred")
                }
        }
        val prefsEditor = prefs.edit()
        prefsEditor.putInt("test", 122)
        prefsEditor.apply()
        Log.d("prefs_check__", prefs.getInt("test",0).toString() )
        Log.d("kakao_key_check__", Utility.getKeyHash(this))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateManager.onResult(requestCode,resultCode, data,this)
    }

    override fun onBackPressed() {}
}