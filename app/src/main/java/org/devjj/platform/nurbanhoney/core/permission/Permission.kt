package org.devjj.platform.nurbanhoney.core.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

const val STORAGE_PERMISSION_REQUEST_CODE = 100

fun isNotPermissionsAllowed(context: Context , permission : String) =
    ContextCompat.checkSelfPermission(
        context,
        permission//Manifest.permission.READ_EXTERNAL_STORAGE
    ) != PackageManager.PERMISSION_GRANTED

fun askReadStoragePermission(activity: Activity) {
    requestPermissions(
        activity,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        STORAGE_PERMISSION_REQUEST_CODE
    )
}