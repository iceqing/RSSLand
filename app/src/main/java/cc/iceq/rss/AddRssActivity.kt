package cc.iceq.rss

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import cc.iceq.rss.model.Feed
import cc.iceq.rss.service.ArticleServiceImpl
import cc.iceq.rss.ui.home.HomeViewModel
import cc.iceq.rss.util.DpUtil
import cc.iceq.rss.util.ToastUtil
import com.rometools.rome.feed.synd.SyndFeed
import kotlinx.android.synthetic.main.content_note.*
import kotlinx.coroutines.*
import java.net.URL

class AddRssActivity : AppCompatActivity() {
    val articleService = ArticleServiceImpl()

    private lateinit var homeViewModel: HomeViewModel

    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("error", "NoteActivity error when request", throwable)
        // 发生异常时的捕获
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Add RSS"
        }
        actionBar!!.setDisplayShowTitleEnabled(true)
        // 决定左上角图标的右侧是否有向左的小箭头, true
        actionBar.setDisplayHomeAsUpEnabled(true)
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val btn: Button = note_search_btn
        val context = this
        btn.setOnClickListener {
            GlobalScope.launch(errorHandler) {
                withContext(Dispatchers.IO) {
                    val url = rss_url_text.text.toString()
                    Log.i("INFO", "homeViewModel url is $url")
                    val syncFeed = articleService.findSyncFeedByUrl(url)
                    var decodeStream: Bitmap? = null;

                    if(syncFeed!=null && syncFeed.icon!=null) {
                        val iconUrl = URL(syncFeed.icon.url)
                        decodeStream = BitmapFactory.decodeStream(iconUrl.openStream())

                    }
                    refreshUi(url, syncFeed, context, decodeStream)
                }
            }
        }
    }

    private suspend fun refreshUi(
        url: String,
        syncFeed: SyndFeed?,
        context: AddRssActivity,
        decodeStream: Bitmap?
    ) {
        withContext(Dispatchers.Main) {
            if (syncFeed != null) {
                search_result.removeAllViews()
                val articleLayout = LayoutInflater.from(context)
                    .inflate(R.layout.search_rss_layout, null) as ConstraintLayout

                if (decodeStream!=null) {
                    val imageView: ImageView = articleLayout.findViewById(R.id.search_rss_icon_img)
                    imageView.setImageBitmap(decodeStream)
                }


                val textView: TextView = articleLayout.findViewById(R.id.search_rss_title)

                textView.text = syncFeed.title

                val dpToPixel = DpUtil.dpToPixel(60f, resources)

                textView.height = dpToPixel
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                textView.text = syncFeed.title
                textView.gravity = Gravity.CENTER_VERTICAL

                val textView2: TextView =
                    articleLayout.findViewById(R.id.search_rss_url)
                textView2.text = syncFeed.link
                articleLayout.background =
                    resources.getDrawable(R.drawable.main_list_item, theme)
                search_result.addView(articleLayout)
                articleLayout.setOnClickListener {
                    var feed = Feed(syncFeed.title, rss_url_text.text.toString(), syncFeed.title);
                    articleService.insert(feed)
                    ToastUtil.showShortText("保存成功")
                    context.finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //监听左上角的返回箭头
        val home = android.R.id.home
        val itemId = item.itemId
        Log.i("[INFO] ", "home: $home， itemId: $itemId")

        if (itemId == home) {
            Log.i("[INFO] ", "item eq home id")
            this.finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
