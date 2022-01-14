package com.sokhorn.bgapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import com.sokhorn.bgapplication.request.ApiClient
import io.realm.Realm

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiClient.initOkHttp(Utils.getAndroidId(this))
        Realm.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    companion object {
        var isInternetConnected = true
        var activityVisible = false


        fun onActivityResume() {
            activityVisible = true
        }

        fun onActivityPause() {
            activityVisible = false
        }
    }
}

object Utils {
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    const val BASE_DATE_FORMAT = "dd.MM.yyyy"
    const val BASE_DATE_FORMAT_API = "yyyy-MM-dd"
    const val BASE_DATE_FORMAT_API_NEW = "yyyy-MM-dd HH:mm:ss"
    const val BASE_DATE_FORMAT_API_NEW_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    const val NUMBER_FORMAT_QTY = "#,##0.00"

    const val TIME_OUT = 90.0
}