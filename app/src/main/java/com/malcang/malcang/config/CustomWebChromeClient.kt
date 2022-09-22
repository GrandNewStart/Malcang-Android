package com.malcang.malcang.config

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.view.isVisible

class CustomWebChromeClient(private val context: Context): WebChromeClient() {

//    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
//        Log.d("CustomWebChromeClient", consoleMessage?.message() ?: "")
//        return super.onConsoleMessage(consoleMessage)
//    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        WebView(context).let { webView ->
            webView.settings.apply {
                userAgentString = "malcangApp/Android"
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
            }
            webView.webViewClient = CustomWebViewClient()
            webView.webChromeClient = CustomWebChromeClient(context)
            (resultMsg?.obj as? WebView.WebViewTransport)?.webView = webView
            resultMsg?.sendToTarget()
            val dialog = Dialog(context)
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
        AlertDialog.Builder(context)
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
        AlertDialog.Builder(context)
            .setTitle("")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> result?.cancel() }
            .create()
            .show()
        return true
    }

}