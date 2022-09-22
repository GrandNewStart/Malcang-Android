package com.malcang.malcang.managers

import android.util.Log
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.config.jwt
import com.malcang.malcang.config.uuid
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object APIManager {

    private const val TAG = "APIManager"
    private const val BASE_URL = "https://www.malcang.com"
    private val client = OkHttpClient()

    fun setFCMToken(token: String, completionHandler: (error: String?) -> Unit = {}) {
        val jwt = MalcangApp.INSTANCE.jwt
        if (jwt == null) {
            Log.e(TAG, "setFCMToken: The device doesn't have jwt. Terminating request.")
            completionHandler("no jwt")
            return
        }
        val json = JSONObject()
        json.put("fcm_token", token)
        json.put("device_id", MalcangApp.INSTANCE.uuid)
        val requestBody = json.toString().toRequestBody("application/json;charset-utf8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/v1/fcm/saveToken")
            .post(requestBody)
            .header("token", jwt)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "setFCMToken: onResponse - FAILURE - ${e.localizedMessage}")
                completionHandler(e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    if (response.code == 200) {
                        val responseJson = JSONObject(it.string())
                        val code = responseJson.get("code") as String
                        if (code == "1") {
                            Log.d(TAG, "setFCMToken: onResponse - SUCCESS - code:$code")
                            completionHandler(null)
                        } else {
                            Log.d(TAG, "setFCMToken: onResponse - FAILURE - code:$code")
                            completionHandler("unknown error")
                        }
                    }
                }
            }
        })
    }

    fun getFCMToken(completionHandler: (error: String?, token: String?) -> Unit = { _, _ -> }) {
        val jwt = MalcangApp.INSTANCE.jwt
        if (jwt == null) {
            Log.e(TAG, "getFCMToken: The device doesn't have jwt. Terminating request.")
            completionHandler("no jwt", null)
            return
        }
        val request = Request.Builder()
            .url("$BASE_URL/v1/fcm/getToken")
            .get()
            .header("token", jwt)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "getFCMToken: onResponse - FAILURE - ${e.localizedMessage}")
                completionHandler(e.localizedMessage, null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    Log.d(TAG, response.toString())
                    if (response.code == 200) {
                        val responseJson = JSONObject(it.string())
                        val code = responseJson.get("code") as String
                        if (code == "1") {
                            val fcmToken = responseJson.get("fcm_token") as String
//                        val mmNo = responseJson.get("mm_no") as String
//                        val deviceId = responseJson.get("device_id") as String
                            Log.d(TAG, "getFCMToken: onResponse - SUCCESS - $responseJson")
                            completionHandler(null, fcmToken)
                        } else {
                            Log.d(TAG, "getFCMToken: onResponse - FAILURE - $responseJson")
                            completionHandler("unknown error", null)
                        }
                    }
                }
            }
        })
    }

    fun updateFCMToken(token: String, completionHandler: (error: String?) -> Unit = {}) {
        val jwt = MalcangApp.INSTANCE.jwt
        if (jwt == null) {
            Log.e(TAG, "updateFCMToken: The device doesn't have jwt. Terminating request.")
            completionHandler("no jwt")
            return
        }
        val json = JSONObject()
        json.put("fcm_token", token)
        json.put("device_id", MalcangApp.INSTANCE.uuid)
        val requestBody = json.toString().toRequestBody("application/json;charset-utf8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/v1/fcm/refreshToken")
            .put(requestBody)
            .header("token", jwt)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "updateFCMToken: onResponse - FAILURE - ${e.localizedMessage}")
                completionHandler(e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    if (response.code == 200) {
                        val responseJson = JSONObject(it.string())
                        val code = responseJson.get("code") as String
                        if (code == "1") {
                            Log.d(TAG, "updateFCMToken: onResponse - SUCCESS - code:$code")
                            completionHandler(null)
                        } else {
                            Log.d(TAG, "updateFCMToken: onResponse - FAILURE - code:$code")
                            completionHandler("unknown error")
                        }
                    }
                }
            }
        })
    }

    fun deleteFCMToken(completionHandler: (error: String?) -> Unit = {}) {
        val jwt = MalcangApp.INSTANCE.jwt
        if (jwt == null) {
            Log.e(TAG, "deleteFCMToken: The device doesn't have jwt. Terminating request.")
            completionHandler("no jwt")
            return
        }
        val json = JSONObject()
        json.put("device_id", MalcangApp.INSTANCE.uuid)
        val requestBody = json.toString().toRequestBody("application/json;charset-utf8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/v1/fcm/deleteToken")
            .put(requestBody)
            .header("token", jwt)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "deleteFCMToken: onResponse - FAILURE - ${e.localizedMessage}")
                completionHandler(e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    if (response.code == 200) {
                        val responseJson = JSONObject(it.string())
                        val code = responseJson.get("code") as String
                        if (code == "1") {
                            Log.d(TAG, "deleteFCMToken: onResponse - SUCCESS - code:$code")
                            completionHandler(null)
                        } else {
                            Log.d(TAG, "deleteFCMToken: onResponse - FAILURE - code:$code")
                            completionHandler("unknown error")
                        }
                    }
                }
            }
        })
    }

}