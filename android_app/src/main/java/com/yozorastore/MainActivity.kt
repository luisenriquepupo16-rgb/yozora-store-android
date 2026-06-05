package com.yozorastore

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var isOfflineMode = false
    private var isFirstLoad = true
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (isFirstLoad) {
            setTheme(R.style.Theme_Splash)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                progressBar.visibility = View.VISIBLE
            }
            
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                
                if (isFirstLoad) {
                    isFirstLoad = false
                }
                
                if (isOfflineMode) {
                    Toast.makeText(this@MainActivity, "Modo offline - Mostrando datos guardados", Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                
                if (!isNetworkAvailable()) {
                    isOfflineMode = true
                    Toast.makeText(this@MainActivity, "Sin conexión - Cargando datos guardados", Toast.LENGTH_LONG).show()
                }
            }
        }
        
        swipeRefresh.setOnRefreshListener {
            if (isNetworkAvailable()) {
                isOfflineMode = false
                webView.reload()
            } else {
                swipeRefresh.isRefreshing = false
                Toast.makeText(this@MainActivity, "Sin conexión. No se puede actualizar.", Toast.LENGTH_SHORT).show()
            }
        }
        
        // URL de Yozora Store (GitHub Pages)
        val url = "https://luisenriquepupo16-rgb.github.io/Yozora-Catalog/"
        
        if (!isNetworkAvailable()) {
            isOfflineMode = true
            Toast.makeText(this@MainActivity, "Sin conexión - Cargando desde caché", Toast.LENGTH_LONG).show()
        }
        
        webView.loadUrl(url)
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    
    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                   caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            @Suppress("DEPRECATION")
            val info = cm.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return info.isConnected
        }
    }
}