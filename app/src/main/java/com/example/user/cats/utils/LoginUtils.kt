package com.example.user.cats.utils

import android.content.SharedPreferences

const val LOGIN = "login"
const val PASSWORD = "password"

class LoginUtils(val sharedPref: SharedPreferences) {
    fun getLogin():String{
        return sharedPref.getString(LOGIN, "")
    }

    fun getPassword():String{
        return sharedPref.getString(PASSWORD, "")
    }

    fun setLogin(login: String){
        sharedPref.edit().
            putString(LOGIN, login).
            apply()

    }

    fun setPassword(password: String){
        sharedPref.edit().
            putString(PASSWORD, password).
            apply()

    }
}