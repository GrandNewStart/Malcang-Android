package com.malcang.malcang.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.malcang.malcang.services.FCMService
import com.malcang.malcang.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch {
            delay(2000L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
        startService(Intent(applicationContext, FCMService::class.java))
    }

    companion object {
        const val TAG = "SplashActivity"
    }

}