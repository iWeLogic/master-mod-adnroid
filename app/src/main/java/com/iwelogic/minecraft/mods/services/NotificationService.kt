package com.iwelogic.minecraft.mods.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.ui.MainActivity

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {}

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification == null) {
            return
        }

        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val pi = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, resources.getString(R.string.notification_channel))

        mBuilder.setContentTitle(title)
        mBuilder.setContentText(body)

        mBuilder.setSmallIcon(R.drawable.ic_notification)
        mBuilder.color = ContextCompat.getColor(baseContext, R.color.black)
        mBuilder.setAutoCancel(true)
        mBuilder.priority = NotificationCompat.PRIORITY_MAX
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mBuilder.setSound(sound)
        mBuilder.setDefaults(Notification.DEFAULT_ALL)
        mBuilder.setContentIntent(pi)
        notificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
    }
}