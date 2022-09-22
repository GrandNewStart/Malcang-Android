package com.malcang.malcang.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.malcang.malcang.R
import com.malcang.malcang.config.CustomProgressDialog
import com.malcang.malcang.config.CustomWebChromeClient
import com.malcang.malcang.config.CustomWebViewClient
import com.malcang.malcang.config.WebViewInterface

class MainActivity : AppCompatActivity() {

    init { INSTANCE = this }

    private val webView: WebView by lazy { findViewById(R.id.webView) }
    private var progressDialog: CustomProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWebView()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.url == "https://www.malcang.com/gene/roulette") {
                return false
            }
            if (webView.canGoBack()) {
                webView.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
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
                webViewClient = CustomWebViewClient()
                webChromeClient = CustomWebChromeClient(this@MainActivity)
                addJavascriptInterface(WebViewInterface(), "BRIDGE")
            }
            loadUrl("https://www.malcang.com/main")
        }
    }

    fun setJWTInLocalStorage(jwt: String) {
        webView.evaluateJavascript("javascript:localStorage.setItem('X-Access-Token', '$jwt')", null)
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

    private fun log(string: String) {
        Log.d("DEBUG", "---> $string")
    }

    companion object {
        lateinit var INSTANCE: MainActivity
    }

}