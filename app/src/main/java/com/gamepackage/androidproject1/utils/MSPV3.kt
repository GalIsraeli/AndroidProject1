package com.gamepackage.androidproject1.utils

import android.content.Context
import android.content.SharedPreferences

class MSPV3 {

    private val spFileName = "MySpFile"


    private lateinit var sharedPreferences: SharedPreferences

    private constructor(context: Context) {
        sharedPreferences = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE) ?: return
    }

    companion object{
        @Volatile
        private var instance: MSPV3? = null

        fun init(context: Context): MSPV3 {
            return instance ?: synchronized(this){
                instance ?: MSPV3(context).also {  instance = it }
            }
        }

        fun getInstance(): MSPV3 {
            return instance ?: throw IllegalStateException(
                "MSPV3 must be initialized by calling init(context) before use."
            )
        }
    }

    fun putString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            commit()
        }
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
}