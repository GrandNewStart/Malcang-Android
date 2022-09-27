package com.malcang.malcang.config

import android.util.Log
import android.webkit.JavascriptInterface
import com.malcang.malcang.managers.APIManager
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.activities.MainActivity

class WebViewInterface {

    @JavascriptInterface
    fun provideJWT(jwt: String) {
        MainActivity.INSTANCE.runOnUiThread {
            MalcangApp.INSTANCE.jwt = jwt
            MainActivity.INSTANCE.setJWTInLocalStorage(jwt)

            val fcmToken = MalcangApp.INSTANCE.fcmToken ?: return@runOnUiThread

            APIManager.getFCMToken { _, token ->
                if (token == null) {
                    Log.d(TAG, "provideJWT: server has no fcmToken for this device.")
                    APIManager.setFCMToken(fcmToken) { error ->
                        if (error == null) {
                            Log.d(TAG, "provideJWT: fcm token successfully set.")
                        } else {
                            Log.e(TAG, "provideJWT: failed to set fcm token: $error")
                        }
                    }
                } else {
                    Log.d(TAG, "provideJWT: server has fcmToken for this device. Deleting...")
                    APIManager.updateFCMToken(fcmToken) { error ->
                        if (error == null) {
                            Log.d(TAG, "provideJWT: fcm token successfully updated.")
                        } else {
                            Log.e(TAG, "provideJWT: failed to update fcm token: $error")
                        }
                    }
                }
            }
        }
    }

    @JavascriptInterface
    fun removeJWT() {
        MainActivity.INSTANCE.runOnUiThread {
            MalcangApp.INSTANCE.jwt = null
            MainActivity.INSTANCE.removeJWTInLocalStorage()
        }
    }

    companion object {
        const val TAG = "WebViewInterface"
    }

}