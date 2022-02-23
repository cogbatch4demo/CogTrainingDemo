package com.example.cogtrainingdemo.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.cogtrainingdemo.PREFS_FILE
import com.example.cogtrainingdemo.PREFS_CHARACTER_KEY
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LocalRepo {
    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    fun hasCharacters(context: Context): Boolean = getPrefs(context).contains(PREFS_CHARACTER_KEY)

    fun setCharacters(context: Context, characters: List<CharactersItem>) {
        getPrefs(context).edit().apply {
            val type = object : TypeToken<List<CharactersItem>>() {}.type
            putString(PREFS_CHARACTER_KEY, Gson().toJson(characters, type))
            apply()
        }
    }

    fun getCharacters(context: Context): List<CharactersItem> {
        getPrefs(context).apply {
            if (contains(PREFS_CHARACTER_KEY)) {
                val json = getString(PREFS_CHARACTER_KEY, "[]")
                val type = object : TypeToken<List<CharactersItem>>() {}.type
                return Gson().fromJson(json, type)
            }
        }

        return emptyList()
    }
}
