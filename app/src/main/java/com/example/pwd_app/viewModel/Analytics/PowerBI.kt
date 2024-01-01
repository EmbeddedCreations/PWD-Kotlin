package com.example.pwd_app.viewModel.Analytics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.pwd_app.R
import android.webkit.WebViewClient

class PowerBI : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power_bi)
        webView = findViewById(R.id.webView)
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

        // this will load the url of the website
        webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiMmYxYWExNWItMjlkZS00ZDg4LWI1ZjYtOWQ4MDlhODFhMTU5IiwidCI6ImVlM2MxNDhmLWRlNGMtNDU0Yy04NzUxLTBlZGU1N2M1N2ZhYiJ9&embedImagePlaceholder=true")

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)
    }
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}
