package com.taeyeon.core

import android.annotation.SuppressLint
import android.util.Log
import com.taeyeon.schoolviolencebreaker.R
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import java.time.LocalDateTime

@SuppressLint("NewApi")
class Error(exception: Exception, occurTime: LocalDateTime = LocalDateTime.now()) {
    val exception: Exception
    val occurTime: LocalDateTime

    val toString: String
    val stackTraceElements: Array<StackTraceElement>
    val stackTraceElement: StackTraceElement
    val className: String
    val fileName: String
    val lineNumber: Int
    val methodName: String
    val isNativeMethod: Boolean
    val message: String
    val full: String

    init {
        this.exception = exception
        this.occurTime = occurTime

        this.toString = exception.toString()
        this.stackTraceElements = exception.stackTrace
        this.stackTraceElement = this.stackTraceElements[0]
        this.className = stackTraceElement.className
        this.fileName = stackTraceElement.fileName
        this.lineNumber = stackTraceElement.lineNumber
        this.methodName = stackTraceElement.methodName
        this.isNativeMethod = stackTraceElement.isNativeMethod
        this.message = exception.message ?: ""

        val stringWriter = StringWriter()
        exception.printStackTrace(PrintWriter(stringWriter))
        this.full = stringWriter.toString()
    }

    companion object {
        const val LOG_TAG = "ERROR_LOG"

        fun log(exception: Exception) {
            Log.e(LOG_TAG, Error(exception).full)
        }

        fun toast(exception: Exception) {
            log(exception)
            Utils.toast(Core.getContext().resources.getString(R.string.error_happen, exception.toString()))
        }

    }
}