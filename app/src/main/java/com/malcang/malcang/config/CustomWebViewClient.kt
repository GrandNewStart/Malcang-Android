package com.malcang.malcang.config

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.activities.MainActivity
import com.malcang.malcang.activities.WebViewActivity

class CustomWebViewClient(private val activity: WebViewActivity): WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        MalcangApp.INSTANCE.jwt?.let { jwt ->
            MainActivity.INSTANCE.setJWTInLocalStorage(jwt)
        }
        activity.showProgressBar(true)
        log("onPageStarted: $url")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        activity.showProgressBar(false)
        log("onPageFinished: $url")
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        const val TAG = "CustomWebViewClient"
    }
}