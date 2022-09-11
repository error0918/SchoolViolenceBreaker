package com.taeyeon.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context


@SuppressLint("StaticFieldLeak")
object Core {
    private var isSetUp = false

    private lateinit var context: Context
    private lateinit var activity: Activity


    fun isSetUp(): Boolean = isSetUp

    fun getContext(): Context = context

    fun getActivity(): Activity = activity


    fun activityCreated(activity: Activity) {
        Core.activity = activity
    }

    fun initialize(context: Context) {
        if (!isSetUp) {
            this.context = context

            Info.initialize()
            Settings.initialize()
            SharedPreferencesManager.Companion.Public.initialize()

            isSetUp = true
        }
    }
}