package com.malcang.malcang.config

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Message
import android.view.KeyEvent
import android.webkit.*
import androidx.core.view.isVisible
import com.malcang.malcang.activities.WebViewActivity

class CustomWebChromeClient(private val activity: WebViewActivity): WebChromeClient() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        WebView(activity).let { webView ->
            webView.settings.apply {
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
            webView.webViewClient = CustomWebViewClient(activity)
            webView.webChromeClient = CustomWebChromeClient(activity)
            (resultMsg?.obj as? WebView.WebViewTransport)?.webView = webView
            resultMsg?.sendToTarget()
            val dialog = Dialog(activity)
            dialog.setContentView(webView)
            dialog.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        dialog.dismiss()
                    }
                    true
                } else {
                    false
                }
            }
            dialog.show()
        }
        return true
    }

    override fun onCloseWindow(window: WebView?) {
        window?.isVisible = false
        window?.destroy()
        super.onCloseWindow(window)
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        AlertDialog.Builder(activity)
            .setTitle("")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
            .create()
            .show()
        return true
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        AlertDialog.Builder(activity)
            .setTitle("")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> result?.cancel() }
            .create()
            .show()
        return true
    }

}