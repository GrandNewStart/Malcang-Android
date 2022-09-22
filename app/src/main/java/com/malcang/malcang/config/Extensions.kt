package com.malcang.malcang.config

import android.content.Context
import android.content.SharedPreferences
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.R
import java.util.*

val MalcangApp.prefs: SharedPreferences
    get() {
        return applicationContext
            .getSharedPreferences(
                getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE
            )
    }

var MalcangApp.fcmToken: String?
    get() {
        return prefs.getString(getString(R.string.key_fcm_token), null)
    }
    set(token) {
        prefs.edit().apply {
            putString(getString(R.string.key_fcm_token), token)
            apply()
        }
    }

var MalcangApp.jwt: String?
    get() {
        return prefs.getString(getString(R.string.key_jwt), null)
    }
    set(token) {
        prefs.edit().apply {
            putString(getString(R.string.key_jwt), token)
            apply()
        }
    }

var MalcangApp.uuid: String
    get() {
        prefs.getString(getString(R.string.ket_uuid), null)?.let { return it }
        val id = UUID.randomUUID().toString()
        uuid = id
        return id
    }
    set(token) {
        prefs.edit().apply {
            putString(getString(R.string.ket_uuid), token)
            apply()
        }
    }