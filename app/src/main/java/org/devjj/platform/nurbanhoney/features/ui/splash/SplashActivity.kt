package org.devjj.platform.nurbanhoney.features.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.sharedpreference.Prefs
import org.devjj.platform.nurbanhoney.features.network.VersionCheckService
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity
@Inject constructor() : AppCompatActivity() {

    private val SPLASH_DISPLAY_TIME = 2000L

    @Inject
    internal lateinit var navigator: Navigator

    @Inject
    internal lateinit var versionCheckService: VersionCheckService

    @Inject
    internal lateinit var updateManager: UpdateManager


    private val viewModel by viewModels<SplashViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            runBlocking { navigator.showHome(this@SplashActivity) }
            finish()
        }, SPLASH_DISPLAY_TIME)

        CoroutineScope(Dispatchers.IO).launch {
            //Log.d("version_check__", versionCheckService.appVersion("nurbanhoney").execute().body().toString())

            var version = versionCheckService.appVersion("nurbanhoney").execute().body().also {
                Log.d("type_check__", "${it?.javaClass}")
                Log.d("type_check__", "${it.toString()}")
            }

            try {
                Log.d("version_check__", version?.version ?: "")
                when (version?.isUpdate) {
                    // 어플이 업로드 되기 전에 테스트 불가능?
                    true -> updateManager.update(activity = this@SplashActivity)
                    //false ->updateManager.update(activity = this@SplashActivity)
                }
            } catch (e: Exception) {
                //val error = Gson().fromJson(json.get("appversion_result").asJsonObject, Error::class.java)
                Log.d("version_check__", "error occurred")
            }
        }
        Log.d("kakao_key_check__", Utility.getKeyHash(this))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateManager.onResult(requestCode, resultCode, data, this)
    }

    override fun onBackPressed() {}

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}