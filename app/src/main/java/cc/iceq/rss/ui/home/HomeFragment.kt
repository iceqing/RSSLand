package cc.iceq.rss.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.rometools.rome.feed.synd.SyndFeed
import kotlinx.coroutines.*
import org.joda.time.DateTime
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class HomeFragment : Fragment() {

    private val sharedViewModel: HomeViewModel by activityViewModels()
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
        refresh()

        sharedViewModel.text.observe(viewLifecycleOwner, Observer {
            Log.i("INFO", "observer refresh##############")
            refresh()
        })
        return root
    }

    override fun onResume() {
        Log.i("[INFO]", "enter HomeFragment!")
        refresh()
        super.onResume()
    }

    private fun refresh() {
        var url = sharedViewModel.text.value.toString()
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                // 执行你的耗时操作代码
                if (null == url || "".equals(url)) {
                    url = "https://iceq.cc/atom.xml"
                }
                Log.i("INFO", "url is " + url)
                var allBtnList = articleService.queryAll(url)
                doOnUiCode(allBtnList);
            }
        }
    }


    private fun dpToPixel(dp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()


    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("error", "error when request11111", throwable)
        // 发生异常时的捕获
    }


    private suspend fun doOnUiCode(feed: SyndFeed) {
        withContext(Dispatchers.Main) {
            // 更新你的UI

//            Log.i("INFO", "allBtnList: $feed")
            val mainLine: LinearLayout = binding.mainLine

            mainLine.removeAllViews()

            if (feed.entries.isEmpty()) {
                Toast.makeText(context, "查询内容为空", Toast.LENGTH_SHORT).show()
            }

            feed.entries.forEach { item ->
                val dpToPixel = dpToPixel(60f)
                val articleLayout = LayoutInflater.from(context)
                    .inflate(R.layout.article_layout, null) as ConstraintLayout
                val textView: TextView = articleLayout.findViewById(R.id.articleTitle)
                textView.height = dpToPixel
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                textView.text = item.title
                textView.gravity = Gravity.CENTER_VERTICAL
                textView.setOnClickListener {
                    Log.i("INFO", "item is " + item)
                    Log.i("INFO", "enter my activity!, url is " + item.uri)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                    startActivity(intent);
                }
                val theme = requireContext().theme
                articleLayout.background = resources.getDrawable(R.drawable.main_list_item, theme)
                val textView2: TextView = articleLayout.findViewById(R.id.articleTimeAndAuthor)
                var author = item.author
                if ((author == null || "".equals(author)) && feed.authors.size > 0) {
                    author = feed.authors[0].name
                }
                textView2.text =
                    "" + DateTime(item.publishedDate.time).toString("yyyy-MM-dd") + " " + author
                Log.i("INFO", "item:$item")
                mainLine.addView(articleLayout)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}