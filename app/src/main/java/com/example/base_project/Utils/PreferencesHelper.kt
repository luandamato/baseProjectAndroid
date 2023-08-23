package com.example.base_project.Utils

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import com.example.base_project.Activities.Login.Model.SignInResponse
import com.example.base_project.Utils.Extensions.toObjectClass
import com.google.gson.Gson

object PreferencesHelper {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private const val SHARED_PREFERENCES_NAME = "$PACKAGE_NAME.SHARED_PREFERENCES"

    private const val PREF_SESSION_COOKIE = "$SHARED_PREFERENCES_NAME.PREF_SESSION_COOKIE"
    private const val PREF_USER_ID = "$SHARED_PREFERENCES_NAME.PREF_USER_ID"
    private const val PREF_DEVICE_ID = "$SHARED_PREFERENCES_NAME.PREF_DEVICE_ID"
    private const val PREF_FIRST_TIME = "$SHARED_PREFERENCES_NAME.PREF_FIRST_TIME"
    private const val PREF_BASIC_AUTH = "$SHARED_PREFERENCES_NAME.PREF_BASIC_AUTH"
    private const val PREF_SESSION = "$SHARED_PREFERENCES_NAME.PREF_SESSION"
    private const val PREF_SELECTED_PLAN = "$SHARED_PREFERENCES_NAME.PREF_SELECTED_PLAN"
    private const val PREF_NOTIFICATIONS_ENABLED = "$SHARED_PREFERENCES_NAME.PREF_NOTIFICATIONS_ENABLED"
    private const val PREF_LOCALIZATION_ENABLED = "$SHARED_PREFERENCES_NAME.PREF_LOCALIZATION_ENABLED"

    var sessionCookie: String?
        get() = sharedPreferences.getString(PREF_SESSION_COOKIE, null)
        set(value) = sharedPreferences.edit().putString(PREF_SESSION_COOKIE, value).apply()

    var basicAuth: String
        get() = sharedPreferences.getString(PREF_BASIC_AUTH, null) ?: ""
        set(value) = sharedPreferences.edit().putString(PREF_BASIC_AUTH, value).apply()

    var userId: String
        get() = sharedPreferences.getString(PREF_USER_ID, null) ?: ""
        set(value) = sharedPreferences.edit().putString(PREF_USER_ID, value).apply()

    var deviceId: String
        get() = sharedPreferences.getString(PREF_DEVICE_ID, null) ?: ""
        set(value) = sharedPreferences.edit().putString(PREF_DEVICE_ID, value).apply()

    var session: SignInResponse?
        get() = sharedPreferences.getString(PREF_SESSION, "{}").toObjectClass(SignInResponse::class.java)
        set(value) = sharedPreferences.edit().putString(PREF_SESSION, Gson().toJson(value)).apply()

    var firstTime: Boolean
        get() {
            val isFirstTime = sharedPreferences.getBoolean(PREF_FIRST_TIME, true)
            firstTime = false
            return isFirstTime
        }
        set(value) = sharedPreferences.edit().putBoolean(PREF_FIRST_TIME, value).apply()

    var areNotificationsEnabled: Boolean
        get() = sharedPreferences.getBoolean(PREF_NOTIFICATIONS_ENABLED, true)
        set(value) = sharedPreferences.edit().putBoolean(PREF_NOTIFICATIONS_ENABLED, value).apply()

    var isLocalizationEnabled: Boolean
        get() = sharedPreferences.getBoolean(PREF_LOCALIZATION_ENABLED, true)
        set(value) = sharedPreferences.edit().putBoolean(PREF_LOCALIZATION_ENABLED, value).apply()

    val isLogged: Boolean
        get() = !sessionCookie.isNullOrBlank()

    fun clearSharedPref() {
        sharedPreferences.edit().clear().apply()
        firstTime = false
    }
}