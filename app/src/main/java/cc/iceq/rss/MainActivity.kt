package cc.iceq.rss

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import cc.iceq.rss.databinding.ActivityMainBinding
import cc.iceq.rss.service.ArticleServiceImpl
import com.rometools.rome.feed.synd.SyndFeed
import kotlinx.coroutines.*
import org.joda.time.DateTime

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var articleService = ArticleServiceImpl();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        Log.i("[INFO]", "enter main activity!")
        val mContext: Context = this;
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                // 执行你的耗时操作代码
                var allBtnList = articleService.queryAll("https://iceq.cc/atom.xml")
                doOnUiCode(allBtnList, mContext);
            }
        }
        super.onResume()
    }

    private fun dpToPixel(dp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()


    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("error", "error when request11111", throwable)
        // 发生异常时的捕获
    }


    private suspend fun doOnUiCode(feed: SyndFeed, context: Context) {
        withContext(Dispatchers.Main) {
            // 更新你的UI
            Log.i("INFO", "allBtnList: $feed")
            val mainLine: LinearLayout = findViewById(R.id.main_line)

            mainLine.removeAllViews()

            if (feed.entries.isEmpty()) {
                Toast.makeText(context, "查询内容为空", Toast.LENGTH_SHORT).show()
            }

            feed.entries.forEach { item ->
                val dpToPixel = dpToPixel(60f)
                val articleLayout = LayoutInflater.from(context).inflate(R.layout.article_layout, null) as ConstraintLayout
                val textView:TextView = articleLayout.findViewById(R.id.articleTitle)
                textView.height = dpToPixel
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                textView.text = item.title
                textView.gravity= Gravity.CENTER_VERTICAL
                textView.setOnClickListener {
                    Log.i("d", "enter my activity!")
                    val intent = Intent(Intent.ACTION_VIEW,	Uri.parse(item.uri));
                    startActivity(intent);
                }
                articleLayout.background = resources.getDrawable(R.drawable.main_list_item, context.theme)
                val textView2:TextView = articleLayout.findViewById(R.id.articleTimeAndAuthor)
                var author = item.author
                if ((author==null || "".equals(author)) && feed.authors.size>0) {
                    author = feed.authors[0].name
                }
                textView2.text="" + DateTime(item.publishedDate.time).toString("yyyy-MM-dd") + " " + author
                Log.i("INFO", "item:$item")
                mainLine.addView(articleLayout)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //监听左上角的返回箭头
        val home = android.R.id.home
        val itemId = item?.itemId

        Log.i("NoteDetailActivity", "itemId: $itemId")
        Log.i("NoteDetailActivity", "home: $home")

        return when (item.itemId) {
            R.id.action_addRss -> {
                Log.i("NoteDetailActivity", "编辑")
                val newIntent = Intent(this@MainActivity, NoteActivity::class.java)
                startActivity(newIntent)
                return true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}