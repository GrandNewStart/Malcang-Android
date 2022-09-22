package com.malcang.malcang

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.malcang.malcang.activities.SplashActivity
import com.malcang.malcang.config.fcmToken
import com.malcang.malcang.config.jwt
import com.malcang.malcang.config.uuid
import com.malcang.malcang.managers.APIManager

class MalcangApp: Application() {

    init { INSTANCE = this }

    override fun onCreate() {
        super.onCreate()
        logLocalData()
        initFirebase()
        checkJWT()
    }

    private fun logLocalData() {
        Log.d(TAG, "logLocalData: jwt - $jwt")
        Log.d(TAG, "logLocalData: fcmToken - $fcmToken")
        Log.d(TAG, "logLocalData: deviceId - $uuid")
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(applicationContext)
    }

    private fun getFCMToken() {
        FirebaseMessaging
            .getInstance()
            .token
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(SplashActivity.TAG, "getFCMToken: token successfully fetched - ${task.result}")
                        fcmToken = task.result
                        return@OnCompleteListener
                    }
                    Log.w(SplashActivity.TAG, "Fetching FCM registration token failed", task.exception)
                }
            )
    }

    private fun checkJWT() {
        if (jwt == null) {
            Log.d(TAG, "user JWT doesn't exist. Checking fcmToken existence in server...")
            APIManager.getFCMToken { _, token ->
                token?.let {
                    Log.d(TAG, "Server has fcmToken for this device. Deleting...")
                    APIManager.deleteFCMToken { error ->
                        error?.let {
                            Log.d(TAG, "Server has failed to remove fcmToken for this device: $it")
                            return@deleteFCMToken
                        }
                        Log.d(TAG, "Server has successfully removed fcmToken for this device")
                    }
                    return@getFCMToken
                }
                Log.d(TAG, "Server has no fcmToken for this device.")
            }
        }
    }

    companion object {
        const val TAG = "MalcangApp"
        lateinit var INSTANCE: MalcangApp
    }

}