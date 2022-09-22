package com.malcang.malcang.managers

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.R
import com.malcang.malcang.activities.SplashActivity

object MalcangNotificationManager {

    private fun getCustomDesign(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews(MalcangApp.INSTANCE.applicationContext.packageName, R.layout.layout_notification)
        remoteViews.setTextViewText(R.id.noti_title, title)
        remoteViews.setTextViewText(R.id.noti_message, message)
        remoteViews.setImageViewResource(R.id.logo, R.mipmap.ic_launcher)
        return remoteViews
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(title: String?, message: String?) {
        MalcangApp.INSTANCE.applicationContext.apply {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)

            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            NotificationCompat.Builder(applicationContext, channelId).apply {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                setSmallIcon(R.mipmap.ic_launcher)
                setSound(uri)
                setAutoCancel(true)
                setVibrate(longArrayOf(1000, 1000, 1000)) //알림시 진동 설정 : 1초 진동, 1초 쉬고, 1초 진동
                setOnlyAlertOnce(true) //동일한 알림은 한번만.. : 확인 하면 다시 울림
                setContentIntent(pendingIntent)
                setContent(getCustomDesign(title!!, message!!))

                val notificationManager = NotificationManagerCompat.from(applicationContext)
                val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.setSound(uri, null)
                notificationManager.createNotificationChannel(notificationChannel)
                notificationManager.notify(0, build())
            }
        }
    }

    fun postNotification(message: RemoteMessage) {
        MalcangApp.INSTANCE.applicationContext.apply {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = NotificationManagerCompat.from(applicationContext)

            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            NotificationCompat.Builder(applicationContext, channelId).apply {
                val title: String = message.notification?.title ?: ""
                val body: String = message.notification?.body ?: ""
                setContentTitle(title)
                setContentText(body)
                setSmallIcon(R.mipmap.ic_launcher)
                val notification: Notification = build()
                notificationManager.notify(1, notification)
            }
        }
    }

}