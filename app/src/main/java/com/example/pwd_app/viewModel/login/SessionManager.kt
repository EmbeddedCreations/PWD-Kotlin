package com.example.pwd_app.viewModel.login

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "UserSession"
        const val KEY_ATC = "atc"
        const val KEY_PO = "po"
        const val KEY_JE = "je"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    fun createLoginSession(atc: String, po: String, je: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_ATC, atc)
        editor.putString(KEY_PO, po)
        editor.putString(KEY_JE, je)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserDetails(): HashMap<String, String> {
        val user = HashMap<String, String>()
        user[KEY_ATC] = sharedPreferences.getString(KEY_ATC, "")!!
        user[KEY_PO] = sharedPreferences.getString(KEY_PO, "")!!
        user[KEY_JE] = sharedPreferences.getString(KEY_JE, "")!!
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }
}
