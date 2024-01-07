package com.iwelogic.minecraft.mods

import android.app.*
import android.os.Build
import com.facebook.appevents.AppEventsLogger
import com.iwelogic.minecraft.mods.manager.FirebaseConfigManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var firebaseRemoteConfigManager: FirebaseConfigManager

    companion object {
        var baseUrl = "https://master-mod.s3.eu-central-1.amazonaws.com"
    }

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