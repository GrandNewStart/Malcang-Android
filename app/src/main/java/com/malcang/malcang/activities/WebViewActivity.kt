package com.malcang.malcang.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.malcang.malcang.R
import com.malcang.malcang.config.CustomProgressDialog
import com.malcang.malcang.config.CustomWebChromeClient
import com.malcang.malcang.config.CustomWebViewClient
import com.malcang.malcang.config.WebViewInterface

open class WebViewActivity: AppCompatActivity() {

    val webView: WebView by lazy { findViewById(R.id.webView) }
    private var progressDialog: CustomProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        WebView.setWebContentsDebuggingEnabled(true)
        webView.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            settings.apply {
                userAgentString = "malcangApp/Android"
                useWideViewPort = true
                allowContentAccess = true
                javaScriptEnabled = true
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                builtInZoomControls = false
                loadWithOverviewMode = true

                cacheMode = WebSettings.LOAD_NO_CACHE
                setSupportMultipleWindows(true)
                setSupportZoom(false)
            }
            webViewClient = CustomWebViewClient(this@WebViewActivity)
            webChromeClient = CustomWebChromeClient(this@WebViewActivity)
            addJavascriptInterface(WebViewInterface(), "BRIDGE")
        }
    }

    fun showProgressBar(show: Boolean) {
        if (show) {
            if (progressDialog == null) {
                progressDialog = CustomProgressDialog(this)
                progressDialog?.setCancelable(false)
                progressDialog?.show()
            }
        } else {
            progressDialog?.let {
                if (it.isShowing) it.dismiss()
                progressDialog = null
            }
        }
    }

}