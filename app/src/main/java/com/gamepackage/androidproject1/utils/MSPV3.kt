package com.gamepackage.androidproject1.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.gamepackage.androidproject1.logic.Score
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MSPV3 private constructor(context: Context) {

    private val spFileName = "MySpFile"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(spFileName, Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: MSPV3? = null

        fun init(context: Context): MSPV3 {
            return instance ?: synchronized(this) {
                instance ?: MSPV3(context).also { instance = it }
            }
        }

        fun getInstance(): MSPV3 {
            return instance ?: throw IllegalStateException(
                "MSPV3 must be initialized by calling init(context) before use."
            )
        }
    }

    fun saveTopScores(scoreList: List<Score>) {
        val gson = Gson()
        val json = gson.toJson(scoreList)
        sharedPreferences.edit {
            putString("top_scores", json)
        }
    }

    fun getTopScores(): List<Score> {
        val gson = Gson()
        val json = sharedPreferences.getString("top_scores", "[]")
        val type = object : TypeToken<List<Score>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addNewScore(newScore: Score) {
        val currentScores = getTopScores().toMutableList()
        currentScores.add(newScore)
        currentScores.sortByDescending { it.score }
        if (currentScores.size > 10) {
            currentScores.subList(10, currentScores.size).clear()
        }
        saveTopScores(currentScores)
    }
}
