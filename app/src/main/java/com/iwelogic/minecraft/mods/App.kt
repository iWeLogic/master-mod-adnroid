package com.iwelogic.minecraft.mods

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp
import java.io.BufferedReader
import java.io.InputStreamReader


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                resources.getString(R.string.notification_channel),
                resources.getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        AppEventsLogger.newLogger(this).logEvent("sentFriendRequest")
    }
}