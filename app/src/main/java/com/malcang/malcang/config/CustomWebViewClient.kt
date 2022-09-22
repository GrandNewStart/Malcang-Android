package com.malcang.malcang.config

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.activities.MainActivity

class CustomWebViewClient: WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        MalcangApp.INSTANCE.jwt?.let { jwt ->
            MainActivity.INSTANCE.setJWTInLocalStorage(jwt)
        }
        MainActivity.INSTANCE.showProgressBar(true)
        log("onPageStarted: $url")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        MainActivity.INSTANCE.showProgressBar(false)
        log("onPageFinished: $url")
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        const val TAG = "CustomWebViewClient"
    }
}