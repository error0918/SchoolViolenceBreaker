package com.taeyeon.core

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/*
 * NOTICE
 *
 * Add "implementation "com.google.code.gson:gson:2.9.1"" at build.gradle (app)
 *
 */

class SharedPreferencesManager(
    name: String = DEFAULT_NAME, mode: Int = DEFAULT_MODE,
    defaultBoolean: Boolean = DEFAULT_BOOLEAN, defaultInt: Int = DEFAULT_INT, defaultFloat: Float = DEFAULT_FLOAT, defaultLong: Long = DEFAULT_LONG, defaultString: String = DEFAULT_STRING
) {

    companion object {

        const val DEFAULT_NAME = "SHARED_PREFERENCES"
        const val DEFAULT_MODE = Context.MODE_PRIVATE

        const val DEFAULT_BOOLEAN = false
        const val DEFAULT_INT = 0
        const val DEFAULT_FLOAT = 0f
        const val DEFAULT_LONG = 0L
        const val DEFAULT_STRING = ""

        val sharedPreferencesManagers = arrayListOf<SharedPreferencesManager>()

        fun clearAllSharedPreferencesManagers() {
            for (sharedPreferencesManager in sharedPreferencesManagers) {
                sharedPreferencesManager.clear()
            }
        }


        object Public {

            private lateinit var sharedPreferencesManager: SharedPreferencesManager


            const val NAME: String = "PUBLIC_SHARED_PREFERENCES_MANAGER"
            const val MODE = Context.MODE_PRIVATE

            var defaultBoolean = DEFAULT_BOOLEAN
            var defaultInt = DEFAULT_INT
            var defaultFloat = DEFAULT_FLOAT
            var defaultLong = DEFAULT_LONG
            var defaultString = DEFAULT_STRING


            fun initialize() {
                if (!Core.isSetUp()) sharedPreferencesManager = SharedPreferencesManager(NAME, MODE)
            }


            fun getPublicSharedPreferencesManager(): SharedPreferencesManager = sharedPreferencesManager


            fun getAll(): Map<String, *> = sharedPreferencesManager.getAll()

            fun clear() = sharedPreferencesManager.clear()

            fun contains(key: String): Boolean = sharedPreferencesManager.contains(key)

            fun remove(key: String) = sharedPreferencesManager.remove(key)


            fun getBoolean(key: String, defaultBoolean: Boolean = Public.defaultBoolean): Boolean = sharedPreferencesManager.getBoolean(key, defaultBoolean)

            fun getInt(key: String, defaultInt: Int = Public.defaultInt): Int = sharedPreferencesManager.getInt(key, defaultInt)

            fun getFloat(key: String, defaultFloat: Float = Public.defaultFloat): Float = sharedPreferencesManager.getFloat(key, defaultFloat)

            fun getLong(key: String, defaultLong: Long = Public.defaultLong): Long = sharedPreferencesManager.getLong(key, defaultLong)

            fun <E> getArrayList(key: String, childClass: Class<E>, defaultArrayList: ArrayList<E> = arrayListOf<E>()): ArrayList<E> = sharedPreferencesManager.getArrayList<E>(key, childClass, defaultArrayList)

            fun getAny(key: String, anyType: Type, defaultAny: Any): Any = sharedPreferencesManager.getAny(key, anyType, defaultAny)


            fun put(datas: Pair<String, *>) = sharedPreferencesManager.put(datas)

            fun put(key: String, data: Any) = sharedPreferencesManager.put(key, data)

            fun putBoolean(key: String, data: Boolean) = sharedPreferencesManager.putBoolean(key, data)

            fun putInt(key: String, data: Int) = sharedPreferencesManager.putInt(key, data)

            fun putFloat(key: String, data: Float) = sharedPreferencesManager.putFloat(key, data)

            fun putLong(key: String, data: Long) = sharedPreferencesManager.putLong(key, data)

            fun putString(key: String, data: String) = sharedPreferencesManager.putString(key, data)

            fun <E> putArrayList(key: String, data: ArrayList<E>) = sharedPreferencesManager.putArrayList(key, data)

            fun putAny(key: String, data: Any) = sharedPreferencesManager.putAny(key, data)

        }

    }


    val name: String
    val mode: Int
    val sharedPreferences: SharedPreferences


    var defaultBoolean: Boolean
    var defaultInt: Int
    var defaultFloat: Float
    var defaultLong: Long
    var defaultString: String


    init {
        this.name = name
        this.mode =
            if (mode == Context.MODE_PRIVATE || mode == Context.MODE_WORLD_READABLE || mode == Context.MODE_WORLD_WRITEABLE) mode
            else DEFAULT_MODE
        sharedPreferences = Core.getContext().getSharedPreferences(name, mode)

        this.defaultBoolean = defaultBoolean
        this.defaultInt= defaultInt
        this.defaultFloat = defaultFloat
        this.defaultLong = defaultLong
        this.defaultString = defaultString

        if (!sharedPreferencesManagers.contains(this)) sharedPreferencesManagers.add(this)
    }


    fun getAll(): Map<String, *> {
        return sharedPreferences.all
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }


    fun getBoolean(key: String, defaultBoolean: Boolean = this.defaultBoolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultBoolean)
    }

    fun getInt(key: String, defaultInt: Int = this.defaultInt): Int {
        return sharedPreferences.getInt(key, defaultInt)
    }

    fun getFloat(key: String, defaultFloat: Float = this.defaultFloat): Float {
        return sharedPreferences.getFloat(key, defaultFloat)
    }

    fun getLong(key: String, defaultLong: Long = this.defaultLong): Long {
        return sharedPreferences.getLong(key, defaultLong)
    }

    fun getString(key: String, defaultString: String = this.defaultString): String {
        return sharedPreferences.getString(key, defaultString) ?: defaultString
    }

    fun <E> getArrayList(key: String, childClass: Class<E>, defaultArrayList: ArrayList<E> = arrayListOf()): ArrayList<E> {
        return try {
            Gson().fromJson(sharedPreferences.getString(key, null), TypeToken.getParameterized(ArrayList::class.java, childClass).type) ?: defaultArrayList
        } catch (exception: Exception) {
            Error.log(exception)
            defaultArrayList
        }
    }

    fun <T> getAny(key: String, anyType: Type, defaultAny: T): T {
        return try {
            Gson().fromJson(sharedPreferences.getString(key, Gson().toJson(defaultAny)), anyType)
        } catch (exception: Exception) {
            Error.log(exception)
            defaultAny
        }
    }


    fun put(vararg datas: Pair<String, *>) {
        for (data in datas) {
            put(data.first, data.second ?: Any())
        }
    }

    fun put(key: String, data: Any) {
        when (data) {
            is Boolean -> putBoolean(key, data)
            is Int -> putInt(key, data)
            is Float -> putFloat(key, data)
            is String -> putString(key, data)
            else -> putAny(key, data)
        }
    }

    fun putBoolean(key: String, data: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, data)
        editor.apply()
    }

    fun putInt(key: String, data: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, data)
        editor.apply()
    }

    fun putFloat(key: String, data: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(key, data)
        editor.apply()
    }

    fun putLong(key: String, data: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, data)
        editor.apply()
    }

    fun putString(key: String, data: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, data)
        editor.apply()
    }

    fun putArrayList(key: String, data: ArrayList<*>) {
        val editor = sharedPreferences.edit()
        editor.putString(key, Gson().toJson(data))
        editor.apply()
    }

    fun putAny(key: String, data: Any) {
        val editor = sharedPreferences.edit()
        editor.putString(key, Gson().toJson(data))
        editor.apply()
    }

}