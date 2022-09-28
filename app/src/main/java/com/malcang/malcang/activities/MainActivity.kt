package com.malcang.malcang.activities

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope

class MainActivity : WebViewActivity() {

    init { INSTANCE = this }

    private var didTapBackButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.loadUrl("https://www.malcang.com/main")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.url == "https://www.malcang.com/gene/roulette") {
                return false
            }
            return if (webView.canGoBack()) {
                webView.goBack()
                true
            } else {
                if (didTapBackButton) {
                    finish()
                } else {
                    Toast.makeText(this, "뒤로가기를 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
                    didTapBackButton = true
                    Thread {
                        Thread.sleep(1500)
                        didTapBackButton = false
                    }.start()

                }
                false
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