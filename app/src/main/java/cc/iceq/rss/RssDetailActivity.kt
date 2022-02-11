package cc.iceq.rss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import cc.iceq.rss.model.Feed
import cc.iceq.rss.service.ArticleServiceImpl
import cc.iceq.rss.util.ToastUtil
import kotlinx.android.synthetic.main.activity_rss_detail.*
import kotlinx.android.synthetic.main.content_rss_detail.*

class RssDetailActivity : AppCompatActivity() {
    val articleService = ArticleServiceImpl()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss_detail)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        actionBar!!.setDisplayShowTitleEnabled(true)
        // 决定左上角图标的右侧是否有向左的小箭头, true
        actionBar.setDisplayHomeAsUpEnabled(true)
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false)
        val findById = findFeed()
        if (findById != null) {
            rss_detail_title_text.setText(findById.name)
            rss_detail_url_text.setText(findById.url)
        }


    }

    private fun findFeed(): Feed? {
        val rssId = intent.getLongExtra("rss_id", -1L)
        val findById = articleService.findById(rssId)
        return findById
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rss_detail_header, menu)
        return true
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
            R.id.rss_detail_save -> {
                this.finish()
                val findById = findFeed()
                if (findById != null) {
                    findById.name = rss_detail_title_text.text.toString().trim()
                    findById.url = rss_detail_url_text.text.toString().trim()
                    articleService.feedDao.updateFeed(findById)
                    ToastUtil.showShortText("已保存")
                } else {
                    ToastUtil.showShortText("保存失败")
                }
                return true
            }

        }
        return true
    }
}