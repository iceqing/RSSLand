package cc.iceq.rss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cc.iceq.rss.databinding.RssListActivityBinding
import cc.iceq.rss.service.ArticleServiceImpl
import cc.iceq.rss.util.DpUtil
import cc.iceq.rss.util.ToastUtil
import kotlinx.android.synthetic.main.content_rss_list.*

class RssListActivity : AppCompatActivity() {

    private lateinit var binding: RssListActivityBinding

    val articleService = ArticleServiceImpl()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RssListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "RSS List"
        }

        actionBar!!.setDisplayShowTitleEnabled(true)
        // 决定左上角图标的右侧是否有向左的小箭头, true
        actionBar.setDisplayHomeAsUpEnabled(true)
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false)
    }


    override fun onResume() {
        Log.i("[INFO]", "enter RssListFragment!")
        refreshUI()
        super.onResume()
    }

    private fun refreshUI() {
        val mainLineRssList = main_line_rssList
        val queryMock = articleService.queryAll()
        mainLineRssList.removeAllViews()
        queryMock.forEach { item ->
            val dpToPixel = DpUtil.dpToPixel(60f, resources)
            val articleLayout =
                LayoutInflater.from(this).inflate(R.layout.article_layout, null) as ConstraintLayout
            val textView: TextView = articleLayout.findViewById(R.id.articleTitle)
            textView.height = dpToPixel
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            textView.text = item.title
            textView.gravity = Gravity.CENTER_VERTICAL
            val textView2: TextView = articleLayout.findViewById(R.id.articleTimeAndAuthor)
            textView2.text = "" + item.url
            Log.i("INFO", "item:$item")
            articleLayout.setOnLongClickListener {
                popupMenu(it, item.id)
                true
            }
            mainLineRssList.addView(articleLayout)
        }
    }


    private fun popupMenu(v: View, id: Long) {
        //定义PopupMenu对象
        val popupMenu = PopupMenu(this, v)
        //设置PopupMenu对象的布局
        popupMenu.getMenuInflater().inflate(R.menu.edit_rss_menu, popupMenu.getMenu())
        //设置PopupMenu的点击事件

        popupMenu.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.delete_rss_sub_item -> {
                    val ret = articleService.feedDao.deleteById(id)
                    val retDetail = articleService.feedDetailDao.deleteByFeedId(id)
                    Log.i("INFO", "DELETE ret: $ret, itemId: ${it.itemId}, retDetail: ${retDetail}")
                    refreshUI()
                    ToastUtil.showShortText("已删除订阅")
                }
                else -> {
                    ToastUtil.showShortText("暂未支持")
                }
            }
            true
        }

        //显示菜单
        popupMenu.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //监听左上角的返回箭头
        val home = android.R.id.home
        val itemId = item.itemId

        Log.i("[INFO] ", "itemId: $itemId")
        Log.i("[INFO] ", "home: $home")

        if (itemId == home) {
            Log.i("[INFO] ", "item eq home id")
            this.finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}