package com.example.productlisttestapp.apiutils

import android.content.Context

class SessionManager(val context: Context) {
    companion object {
        val IsLogin: String = "IsLogin"
        val Name: String = "Name"
    }

    val sharedPreferences = context.getSharedPreferences("Test", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    init {
        editor.apply()
    }

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(IsLogin, false)
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(Name, "")
    }

    fun setLogin(name: String) {
        editor.putString(Name, name)
        editor.putBoolean(IsLogin, true)
        editor.commit()
    }
}