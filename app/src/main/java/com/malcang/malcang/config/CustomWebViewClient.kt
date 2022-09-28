package com.malcang.malcang.config

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.activities.MainActivity
import com.malcang.malcang.activities.WebViewActivity
import java.lang.Exception

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

    @SuppressLint("QueryPermissionsNeeded")
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.url?.let { url ->
            if (url.scheme == "kakaotalk") {
                val intent = Intent(ACTION_VIEW, url)
                intent.resolveActivity(activity.packageManager)?.apply {
                    activity.startActivity(intent)
                    return true
                }
                Toast.makeText(activity, "카카오톡이 설치되어있지 않습니다", Toast.LENGTH_SHORT).show()
                activity.startActivity(Intent(ACTION_VIEW, Uri.parse("market://details?id=com.kakao.talk")))
                return true
            }
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        const val TAG = "CustomWebViewClient"
    }
    
}