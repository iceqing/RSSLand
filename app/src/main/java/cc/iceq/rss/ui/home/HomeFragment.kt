package cc.iceq.rss.ui.home

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import cc.iceq.rss.R
import cc.iceq.rss.service.ArticleServiceImpl
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import cc.iceq.rss.InnerWebView
import cc.iceq.rss.databinding.FragmentHomeBinding
import cc.iceq.rss.model.FeedDetail
import cc.iceq.rss.util.DpUtil.dpToPixel
import cc.iceq.rss.util.RefreshUtil
import cc.iceq.rss.util.ToastUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import org.joda.time.DateTime

class HomeFragment : Fragment() {
    private val sharedViewModel: FeedIdModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null

    private val pageModel: PageModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var articleService = ArticleServiceImpl();
    val pageSize = 15

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
        val refreshLayout = binding.refreshLayout
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setEnableAutoLoadMore(true)
        refreshLayout.setRefreshFooter(ClassicsFooter(context));
        refreshLayout.setOnRefreshListener { refreshlayout ->
            refreshDirect()
            refreshlayout.finishRefresh()
        }

        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            loadMore()
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

        val pageNo = pageModel.pageNo.value
        var currentSize = pageSize;
        if (pageNo != null) {
            currentSize = pageNo * pageSize
        }

        val feedList = articleService.findFeedDetailById(id.toLong(), 0, currentSize)
        doOnUiCode(feedList)
        RefreshUtil.refresh(id.toLong())
    }


    private fun refreshDirect() {
        val id = sharedViewModel.text.value.toString()
        val refreshCallback = {
            val pageNo = pageModel.pageNo.value
            var currentSize = pageSize;
            if (pageNo != null) {
                currentSize = pageNo * pageSize
            }
            val feedList = articleService.findFeedDetailById(id.toLong(), 0, currentSize)
            doOnUiCode(feedList)
        }
        RefreshUtil.refresh(id.toLong(), refreshCallback)
    }


    private fun loadMore() {
        val id = sharedViewModel.text.value.toString()
        val pageNo = pageModel.pageNo.value
        if (pageNo != null) {
            val feedList = articleService.findFeedDetailById(id.toLong(),0,pageNo*pageSize)
            if (feedList.size == pageNo * pageSize) {
                pageModel.postPage(pageNo + 1)
            }else {
                ToastUtil.showShortText("我们是有底线的")
            }
            doOnUiCode(feedList)
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
                val sharedPreferences: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context)
                val openUrlPreferences = sharedPreferences.getString("open_url_preference", "auto")
                Log.i("INFO", "openUrlPreferences: $openUrlPreferences")
                if ("inner" == openUrlPreferences) {
                    val intent = Intent(context, InnerWebView::class.java)
                    intent.putExtra("url", item.url)
                    intent.putExtra("title", item.title)
                    startActivity(intent);
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url));
                    startActivity(intent);
                }
            }

            articleLayout.setOnLongClickListener {
                popupMenu(it, item)
                true
            }
            val textView2: TextView = articleLayout.findViewById(R.id.articleTimeAndAuthor)
            val author = item.author
            val pubDate = DateTime(item.publishTime).toString("yyyy-MM-dd")
            textView2.text = "$pubDate / $author"
            Log.i("INFO", "item: $item")
            mainLine.addView(articleLayout)
        }

    }

    private fun popupMenu(v: View?, item: FeedDetail) {
        //定义PopupMenu对象
        val popupMenu = PopupMenu(context, v)
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.edit_feed_menu, popupMenu.getMenu())
        //设置PopupMenu的点击事件
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.copy_rss_sub_item -> {
                    val cm: ClipboardManager =
                        context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val mClipData = ClipData.newPlainText("Label", item.url)
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData)
                    Log.i("INFO", "copy ret: ${item.url}")
                    ToastUtil.showShortText("已复制")
                }
            }
            true
        }

        //显示菜单
        popupMenu.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}