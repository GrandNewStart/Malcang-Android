package com.malcang.malcang.config

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.malcang.malcang.MalcangApp
import com.malcang.malcang.activities.MainActivity
import com.malcang.malcang.activities.WebViewActivity
import java.net.URISyntaxException

class CustomWebViewClient(private val activity: WebViewActivity) : WebViewClient() {

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
        val url = request?.url?.toString() ?: return false
        if (url.startsWith("intent://")) {
            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                intent.getPackage()?.let {
                    activity.startActivity(intent)
                    return true
                }
                val marketIntent = Intent(Intent.ACTION_VIEW)
                marketIntent.data = Uri.parse("market://details?id=" + intent.getPackage()!!)
                activity.startActivity(marketIntent)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (url.startsWith("market://")) {
            try {
                Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    ?.let { intent -> activity.startActivity(intent) }
                return true
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
        view?.loadUrl(url)
        return false
    }

    private fun log(message: String) {
        Log.d("CustomWebViewClient", "---> $message")
    }

}