package com.taeyeon.core

import com.taeyeon.schoolviolencebreaker.R

object Info {
    private var ApplicationName: String = "ApplicationName"
    private var ApplicationExplanation: String = "ApplicationExplanation"
    private var Package: String = "Package"
    private var VersionName: String = "VersionName"
    private var VersionCode: Int = 0
    private var Maker: String = "Maker"

    fun getApplicationName(): String = ApplicationName

    fun getApplicationExplanation(): String = ApplicationExplanation

    fun getPackage(): String = Package

    fun getVersionName(): String = VersionName

    fun getVersionCode(): Int = VersionCode

    fun getMaker(): String = Maker

    fun initialize() {
        if (!Core.isSetUp()) {
            try {
                val packageManager = Core.getContext().packageManager
                val packageInfo = packageManager.getPackageInfo(Core.getContext().packageName, 0)
                val applicationInfo = Core.getContext().applicationInfo

                ApplicationName = packageManager.getApplicationLabel(applicationInfo).toString()
                ApplicationExplanation = Core.getContext().resources.getString(R.string.app_explanation)
                Package = Core.getContext().packageName
                VersionName = packageInfo.versionName
                VersionCode = packageInfo.versionCode
                Maker = Core.getContext().resources.getString(R.string.maker)
            } catch (exception: Exception) {
                Error.log(exception)
            }
        }
    }
}