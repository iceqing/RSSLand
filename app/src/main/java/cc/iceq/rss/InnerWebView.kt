package cc.iceq.rss

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import cc.iceq.rss.util.ToastUtil
import kotlinx.android.synthetic.main.activity_inner_web_view.*
import kotlinx.android.synthetic.main.inner_webview_content.*


class InnerWebView : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_web_view)
        setSupportActionBar(toolbar)
        val webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean{
                if (request == null) return false
                val url = request.url.toString()
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        return true
                    }
                } catch (e: Exception) {
                    //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面

                    ToastUtil.showShortText("应用未安装App")
                    Log.e("WebView", "shouldOverrideUrlLoading error, url: {$url}", e)
                    return true
                }
                //下面的两种方式选择使用其中一种即可
                view!!.loadUrl(url)
                return true

            }
        }
        webview.setWebViewClient(webViewClient)
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

            R.id.rss_detail_share -> {
                Log.i("[INFO] ", "item eq share id")
                var share_intent = Intent()
                share_intent.action = Intent.ACTION_SEND //设置分享行为
                val title = intent.getStringExtra("title")
                val url = intent.getStringExtra("url")
                share_intent.setType("text/plain") //设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_SUBJECT, title) //添加分享内容标题
                share_intent.putExtra(Intent.EXTRA_TEXT, "$title  $url") //添加分享内容
                //创建分享的Dialog
                share_intent = Intent.createChooser(share_intent, resources.getString(R.string.share_url))
                startActivity(share_intent)
                return true
            }
        }
        return true
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inner_detail_header, menu)
        return true
    }
}