package cc.iceq.rss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cc.iceq.rss.databinding.RssListActivityBinding
import cc.iceq.rss.service.ArticleServiceImpl
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


    private fun dpToPixel(dp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

    override fun onResume() {
        Log.i("[INFO]", "enter RssListFragment!")
        val mainLineRssList = main_line_rssList
        val queryMock = articleService.queryAll()
        queryMock.forEach {
                item ->
            val dpToPixel = dpToPixel(60f)
            val articleLayout = LayoutInflater.from(this).inflate(R.layout.article_layout, null) as ConstraintLayout
            val textView: TextView = articleLayout.findViewById(R.id.articleTitle)
            textView.height = dpToPixel
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            textView.text = item.title
            textView.gravity= Gravity.CENTER_VERTICAL
            val theme = this.theme
            articleLayout.background = resources.getDrawable(R.drawable.main_list_item, theme)
            val textView2: TextView = articleLayout.findViewById(R.id.articleTimeAndAuthor)
            var author = item.author
            author =item.author
            textView2.text="" + author
            Log.i("INFO", "item:$item")
            mainLineRssList.addView(articleLayout)
        }
        super.onResume()
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