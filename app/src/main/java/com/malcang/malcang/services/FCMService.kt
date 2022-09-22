package com.malcang.malcang.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.malcang.malcang.managers.APIManager
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.config.fcmToken
import com.malcang.malcang.managers.MalcangNotificationManager


class FCMService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        MalcangApp.INSTANCE.fcmToken = token
        APIManager.getFCMToken { _, t ->
            if (t == null) {
                Log.d(TAG, "onNewToken: server doesn't have fcm token for this device")
                APIManager.setFCMToken(token) { e ->
                    if (e == null) {
                        Log.d(TAG, "onNewToken: fcm token successfully set")
                    } else {
                        Log.e(TAG, "onNewToken: failed to set fcm token")
                    }
                }
            } else {
                Log.d(TAG, "onNewToken: server already has fcm token for this device")
                APIManager.updateFCMToken(token) { e ->
                    if (e == null) {
                        Log.d(TAG, "onNewToken: fcm token successfully updated")
                    } else {
                        Log.e(TAG, "onNewToken: failed to update fcm token")
                    }
                }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        logRemoteMessage(message)
        if (message.data.isNotEmpty()) {
            MalcangNotificationManager.showNotification(
                message.data["title"],
                message.data["body"]
            )
        }
    }

    private fun logRemoteMessage(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: id - ${message.messageId}")
        Log.d(TAG, "onMessageReceived: type - ${message.messageType}")
        message.data.forEach { (key, value) ->
            Log.d(TAG, "onMessageReceived: data - { key: $key, value: $value }")
        }
    }

    companion object {
        const val TAG = "FCMService"
    }

}