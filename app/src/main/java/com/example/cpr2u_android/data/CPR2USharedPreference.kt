package com.example.cpr2u_android.data

import android.content.Context
import android.content.SharedPreferences

object CPR2USharedPreference {
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    private const val REFRESH_TOKEN = "REFRESH_TOKEN"
    private const val DEVICE_TOKEN = "DEVICE_TOKEN"
    private const val IS_LOGIN = "IS_LOGIN"
    private const val USER_NAME = "USER_NAME"
    lateinit var preferences: SharedPreferences
    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun getIsLogin(): Boolean {
        return preferences.getBoolean(IS_LOGIN, false)
    }

    fun setIsLogin(value: Boolean) {
        preferences.edit().putBoolean(IS_LOGIN, value).apply()
    }

    fun getAccessToken(): String {
        return preferences.getString(ACCESS_TOKEN, "") ?: ""
    }

    fun setAccessToken(value: String) {
        preferences.edit().putString(ACCESS_TOKEN, value).apply()
    }

    fun getDeviceToken(): String {
        return preferences.getString(DEVICE_TOKEN, "") ?: ""
    }

    fun setDeviceToken(value: String) {
        preferences.edit().putString(DEVICE_TOKEN, value).apply()
    }

    fun getUserName(): String {
        return preferences.getString(USER_NAME, "") ?: ""
    }

    fun setUserName(value: String) {
        preferences.edit().putString(USER_NAME, value).apply()
    }
}
