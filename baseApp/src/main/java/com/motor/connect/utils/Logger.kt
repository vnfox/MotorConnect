package com.motor.connect.utils

import android.util.Log
import com.motor.connect.BuildConfig

class Logger {
    companion object {
        fun d(message: String, tag: String = "Logger") {
            if (BuildConfig.DEBUG) Log.d(tag, message)
        }

        fun e(message: String, tag: String = "Logger") {
            if (BuildConfig.DEBUG) Log.e(tag, message)
        }

        fun w(message: String, tag: String = "Logger") {
            if (BuildConfig.DEBUG) Log.w(tag, message)
        }
    }
}