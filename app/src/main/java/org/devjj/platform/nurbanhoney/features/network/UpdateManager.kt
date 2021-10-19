package org.devjj.platform.nurbanhoney.features.network

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateManager
@Inject constructor(
    private val appUpdateManager: AppUpdateManager
    ){

    private val REQUEST_CODE = 366
    // appUpdateManager = appUpdateManager
    fun update(activity : Activity){
        Log.d("update_check__", "update checked")
        appUpdateManager.let{
            it.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->

                if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){

                    requestUpdate(appUpdateInfo,activity)
                }
            }
        }
    }

    private fun requestUpdate(appUpdateInfo : AppUpdateInfo, activity : Activity){
        Log.d("update_check__", "update requested")
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            activity,
            REQUEST_CODE
        )
    }

    fun onResult(requestCode : Int, resultCode : Int, data : Intent?,activity: Activity){
        if(requestCode == REQUEST_CODE){
            if (resultCode !== RESULT_OK) {
                Log.d("update_check__", "Update failed! Result code: $resultCode")
                val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                    ) {
                        requestUpdate(appUpdateInfo,activity)
                    }
                }
            }
        }
    }
}