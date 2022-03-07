package org.devjj.platform.nurbanhoney.core.sharedpreference

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.devjj.platform.nurbanhoney.AndroidApplication

object Prefs {
    private const val TOKEN = "NurbanToken"
    private const val USER_ID = "UserId"

    val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(AndroidApplication.instance)
    }
    var token: String
        get() = prefs.getString(TOKEN, null) ?: ""
        set(value) = prefs.edit().putString(TOKEN, value).apply()

    var userId: Int
        get() = prefs.getInt(USER_ID, -1)
        set(value) = prefs.edit().putInt(USER_ID, value).apply()
}