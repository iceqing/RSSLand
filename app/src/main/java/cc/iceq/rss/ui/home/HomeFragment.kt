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
import cc.iceq.rss.service.ArticleServiceImpl
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import cc.iceq.rss.databinding.FragmentHomeBinding
import cc.iceq.rss.model.FeedDetail
import cc.iceq.rss.util.DpUtil.dpToPixel
import cc.iceq.rss.util.RefreshUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
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
        val refreshLayout  = binding.refreshLayout
        refreshLayout.setRefreshHeader(ClassicsHeader(context));
        refreshLayout.setRefreshFooter(ClassicsFooter(context));

        refreshLayout.setOnRefreshListener { refreshlayout ->
            refreshDirect()
            refreshlayout.finishRefresh()
        }

        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            refreshDirect()
            refreshlayout.finishLoadMore()
        }
        return root
    }

    override fun onResume() {
        Log.i("[INFO]", "enter HomeFragment!")
        super.onResume()
    }

    private fun refresh() {
        val id = sharedViewModel.text.value.toString()
        val feedList = articleService.findFeedDetailById(id.toLong())
        doOnUiCode(feedList)
        RefreshUtil.refresh(id.toLong())
    }


    private fun refreshDirect() {
        val id = sharedViewModel.text.value.toString()
        val refreshCallback = {
            val feedList = articleService.findFeedDetailById(id.toLong())
            doOnUiCode(feedList)
        }
        RefreshUtil.refresh(id.toLong(), refreshCallback)
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
            var pubDate = DateTime(item.publishTime).toString("yyyy-MM-dd")
            textView2.text = pubDate + " / " + author
            Log.i("INFO", "item:$item")
            mainLine.addView(articleLayout)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}