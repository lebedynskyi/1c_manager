package com.simple.games.tradeassist.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val keyUser = "UserName"
    private val keyPass = "UserPass"

    private val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)


    fun saveUserName(userName: String) {
        sharedPrefs.edit().putString(keyUser, userName).apply()
    }

    fun savePassword(password: String) {
        sharedPrefs.edit().putString(keyPass, password).apply()
    }

    fun getUserName(): String? {
        return sharedPrefs.getString(keyUser, null)
    }

    fun getPassword(): String? {
        return sharedPrefs.getString(keyPass, null)
    }
}