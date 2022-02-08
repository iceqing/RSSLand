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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import cc.iceq.rss.model.Feed
import cc.iceq.rss.service.ArticleServiceImpl
import cc.iceq.rss.ui.home.FeedIdModel
import cc.iceq.rss.util.DpUtil
import cc.iceq.rss.util.RefreshUtil
import cc.iceq.rss.util.ToastUtil
import kotlinx.android.synthetic.main.content_note.*
import kotlinx.coroutines.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class AddRssActivity : AppCompatActivity() {
    val articleService = ArticleServiceImpl()

    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("error", "NoteActivity error when request", throwable)
        // 发生异常时的捕获
    }

    //这个是共享ViewModel
    private val sharedViewModel: FeedIdModel by viewModels()

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


        val btn: Button = note_search_btn
        btn.setOnClickListener {
            GlobalScope.launch(errorHandler) {
                withContext(Dispatchers.IO) {
                    val urlStr = rss_url_text.text.toString()
                    Log.i("INFO", "homeViewModel url is $urlStr")

                    val urlList = urlStr.split("\n").stream().collect(Collectors.toList())
                    val list = ArrayList<Feed>()

                    for (url in urlList) {
                        val syncFeed = articleService.findSyncFeedByUrl(url)
                        if (syncFeed!=null) {
                            val temp = Feed(syncFeed.title, url, syncFeed.title)
                            list.add(temp)
                        }
                    }
//                    var decodeStream: Bitmap? = null;

//                    if(syncFeed!=null && syncFeed.icon!=null) {
//                        val iconUrl = URL(syncFeed.icon.url)
//                        decodeStream = BitmapFactory.decodeStream(iconUrl.openStream())
//                    }
                    refreshUi(list, null)
                }
            }
        }
    }

    private suspend fun refreshUi(
        syncFeedList: List<Feed>,
        decodeStream: Bitmap?
    ) {
        withContext(Dispatchers.Main) {
            search_result.removeAllViews()
            for (feed in syncFeedList) {
                val articleLayout = LayoutInflater.from(this@AddRssActivity)
                    .inflate(R.layout.search_rss_layout, null) as ConstraintLayout

                val textView: TextView = articleLayout.findViewById(R.id.search_rss_title)

                textView.text = feed.name

                val dpToPixel = DpUtil.dpToPixel(60f, resources)

                textView.height = dpToPixel
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                textView.gravity = Gravity.CENTER_VERTICAL

                val textView2: TextView = articleLayout.findViewById(R.id.search_rss_url)
                textView2.text = feed.url
                articleLayout.background =
                    resources.getDrawable(R.drawable.main_list_item, theme)
                articleLayout.setOnClickListener {
                    val id = articleService.insert(feed)
                    sharedViewModel.postId(id)
                    RefreshUtil.refresh(id)
                    ToastUtil.showShortText("保存成功")
                }
                search_result.addView(articleLayout)
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
