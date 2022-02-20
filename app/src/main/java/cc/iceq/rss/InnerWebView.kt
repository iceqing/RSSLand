package cc.iceq.rss

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_inner_web_view.*
import kotlinx.android.synthetic.main.inner_webview_content.*


class InnerWebView : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inner_web_view)
        setSupportActionBar(toolbar)
        webview.setWebViewClient(WebViewClient())
        webview.settings.javaScriptEnabled=true
        webview.settings.domStorageEnabled=true
        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        if (!url.isNullOrBlank()) {
            webview.loadUrl(url)
        }
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        // 决定左上角图标的右侧是否有向左的小箭头, true
        actionBar.setDisplayHomeAsUpEnabled(true)
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.title = title

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //监听左上角的返回箭头
        val home = android.R.id.home
        val itemId = item.itemId

        Log.i("[INFO] ", "itemId: $itemId, home: $home")

        when (item.itemId) {
            android.R.id.home -> {
                Log.i("[INFO] ", "item eq home id")
                this.finish()
                return true
            }
        }
        return true
    }
}