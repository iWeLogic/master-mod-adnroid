package com.iwelogic.minecraft.mods

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.iwelogic.minecraft.mods.utils.generateKeyHashes


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(resources.getString(R.string.notification_channel), resources.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }
        generateKeyHashes()
        AppEventsLogger.newLogger(this).logEvent("sentFriendRequest")
    }
}