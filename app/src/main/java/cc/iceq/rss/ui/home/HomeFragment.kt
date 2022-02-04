package cc.iceq.rss.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import cc.iceq.rss.R
import cc.iceq.rss.databinding.FragmentHomeBinding
import cc.iceq.rss.service.ArticleServiceImpl
import kotlinx.coroutines.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import cc.iceq.rss.model.FeedDetail
import cc.iceq.rss.util.DpUtil.dpToPixel
import cc.iceq.rss.util.ToastUtil
import com.rometools.rome.feed.synd.SyndFeed
import org.joda.time.DateTime

class HomeFragment : Fragment() {

    private val sharedViewModel: FeedIdModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var articleService = ArticleServiceImpl();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedViewModel.text.observe(viewLifecycleOwner, Observer {
            Log.i("INFO", "HomeFragment sharedViewModel observer refresh, it: ${it}")
            if (it > 0) {
                refresh()
            }
        })
        return root
    }

    override fun onResume() {
        Log.i("[INFO]", "enter HomeFragment!")
//        refresh()
        super.onResume()
    }

    private fun refresh() {
        var id = sharedViewModel.text.value.toString()
        var feedList = articleService.findFeedDetailById(id.toLong())
        doOnUiCode(feedList)

        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                // 执行你的耗时操作代码
                var url = articleService.queryUrlById(id.toLong())
                if (url.isNullOrBlank()) {
                    Log.w("WARN", "url不能为空")
//                    Looper.prepare();
//                    ToastUtil.showShortText("url不能为空")
//                    Looper.loop()
                } else {
                    Log.i("INFO", "url is $url")
                    var syncFeed = articleService.findSyncFeedByUrl(url)
                    saveToDb(id.toLong(), syncFeed)
                }

            }
        }
    }


    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("error", "error when request", throwable)
        // 发生异常时的捕获
    }

    private suspend fun saveToDb(feedId: Long, syncFeed: SyndFeed?) {
        withContext(Dispatchers.Main) {
            syncFeed?.entries?.forEach {
                if (!articleService.containsUrl(it.link)) {
                    var author = it.author
                    if ((author.isNullOrBlank()) && it.authors.size > 0) {
                        author = it.authors[0].name
                    }
                    if (author.isNullOrBlank()) {
                        author = it.title
                    }
                    var pubDate = DateTime(it.publishedDate.time).toString("yyyy-MM-dd")
                    val feedDetail = FeedDetail(it.title, it.link, author, feedId, pubDate)
                    articleService.insert(feedDetail)
                }
            }
        }
    }


    private fun doOnUiCode(feedList: List<FeedDetail>) {
        val mainLine: LinearLayout = binding.mainLine
        mainLine.removeAllViews()

        if (feedList.isEmpty()) {
            Toast.makeText(context, "查询内容为空", Toast.LENGTH_SHORT).show()
        }

        feedList.forEach { item ->
            val dpToPixel = dpToPixel(60f, resources)
            val articleLayout = LayoutInflater.from(context)
                .inflate(R.layout.article_layout, null) as ConstraintLayout
            val textView: TextView = articleLayout.findViewById(R.id.articleTitle)
            textView.height = dpToPixel
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
            textView.text = item.title
            textView.gravity = Gravity.CENTER_VERTICAL
            textView.setOnClickListener {
                Log.i("INFO", "item is $item")
                Log.i("INFO", "enter my activity!, url is " + item.url)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url));
                startActivity(intent);
            }
            val textView2: TextView = articleLayout.findViewById(R.id.articleTimeAndAuthor)
            var author = item.author
            textView2.text = item.pubDate + " / " + author
            Log.i("INFO", "item:$item")
            mainLine.addView(articleLayout)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}