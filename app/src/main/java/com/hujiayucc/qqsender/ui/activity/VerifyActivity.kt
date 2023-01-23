package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.utils.LoginSolver.Companion.verificationResult

class VerifyActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var url: String
    private val gson = JsonParser()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        url = intent.data.toString()
        webView = findViewById(R.id.webview)
        initWebView()
        webView.loadUrl(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webView.evaluateJavascript(
                    """
                    mqq.invoke = function(a,b,c){ return bridge.invoke(a,b,JSON.stringify(c))}"""
                        .trimIndent()
                ) {}
            }
        }
        webView.webChromeClient = object : WebChromeClient() {

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                val msg = consoleMessage?.message()
                // 按下回到qq按钮之后会打印这句话，于是就用这个解决了。。。。
                if (msg?.startsWith("手Q扫码验证") == true) {
                    //TODO
                }
                return super.onConsoleMessage(consoleMessage)
            }
        }
        WebView.setWebContentsDebuggingEnabled(true)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        webView.addJavascriptInterface(Bridge(), "bridge")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()
                return true
            }
        }
        return false
    }

    inner class Bridge {
        @JavascriptInterface
        fun invoke(cls: String?, method: String?, data: String?) {
            if (data != null) {
                val jsData = gson.parse(data)
                if (method == "onVerifyCAPTCHA") {
                    verificationResult.complete(jsData.asJsonObject["ticket"].asString)
                    finish()
                }
            }
        }
    }
}