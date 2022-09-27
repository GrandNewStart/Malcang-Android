package com.malcang.malcang.activities

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent

class MainActivity : WebViewActivity() {

    init { INSTANCE = this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.loadUrl("https://www.malcang.com/main")
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

    fun setJWTInLocalStorage(jwt: String) {
        webView.evaluateJavascript("javascript:localStorage.setItem('X-Access-Token', '$jwt')", null)
    }

    fun removeJWTInLocalStorage() {
        webView.evaluateJavascript("javascript:localStorage.removeItem('X-Access-Token')", null)
    }

    private fun log(string: String) {
        Log.d("DEBUG", "---> $string")
    }

    companion object {
        lateinit var INSTANCE: MainActivity
    }

}