package com.tk.tktask.manager

import android.content.Context
import android.content.SharedPreferences

object PrefManager {

    private lateinit var settings: SharedPreferences

    private const val PREFS_NAME = "prefs"
    const val WORK_MANAGER_STATUS = "WORK_MANAGER_STATUS"

    fun init(context: Context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setPreference(context: Context, key: String?, value: Boolean): Boolean {
        val settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    fun getPreference(context: Context, key: String?): Boolean {
        val settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return settings.getBoolean(key, false)
    }

}