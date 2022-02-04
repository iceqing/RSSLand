package cc.iceq.rss

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import cc.iceq.rss.databinding.ActivityMainBinding
import cc.iceq.rss.model.ArticleInfo
import cc.iceq.rss.service.ArticleServiceImpl
import cc.iceq.rss.ui.home.FeedIdModel
import cc.iceq.rss.util.ThemeUtil

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val articleService = ArticleServiceImpl();

    //这个是共享ViewModel
    private val sharedViewModel: FeedIdModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val currentTheme = sharedPreferences.getString("theme_preference", "auto")
        ThemeUtil.refreshTheme(currentTheme)
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
                R.id.nav_home, R.id.nav_gallery
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { item ->
            Log.i("INFO", "nav start ${item.itemId}, get db url is ${item.itemId}")
            sharedViewModel.postId(item.itemId.toLong())
            drawerLayout.closeDrawers()
            true
        }

        sharedViewModel.text.observe(this, Observer {
            Log.i("INFO", "MainActivity sharedViewModel observer refresh")
            binding.appBarMain.toolbar.title = articleService.queryNameById(it)
        })
    }

    override fun onResume() {
        Log.i("[INFO]", "enter main activity!")

        val queryAll = articleService.queryAll()
        val menu = binding.navView.menu

        if (menu.isNotEmpty()) {
            menu.removeGroup(10001)
            addMenu(queryAll)
        } else {
            addMenu(queryAll)
            if (queryAll.isNotEmpty()) {
                binding.navView.setCheckedItem(menu[0])
                sharedViewModel.postId(menu[0].itemId.toLong())
            }
        }

        super.onResume()
    }

    private fun addMenu(queryAll: ArrayList<ArticleInfo>) {
        queryAll.forEach { item ->
            val itemId: Int = (item.id).toInt()
            binding.navView.menu.add(10001, itemId, 1, item.title)
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
                Log.i("NoteActivity", "编辑")
                val newIntent = Intent(this@MainActivity, AddRssActivity::class.java)
                startActivity(newIntent)
                return true
            }
            R.id.action_rssList -> {
                Log.i("RssListActivity", "编辑")
                val newIntent = Intent(this@MainActivity, RssListActivity::class.java)
                startActivity(newIntent)
                return true
            }
            R.id.action_settings -> {
                Log.i("RssListActivity", "编辑")
                val newIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(newIntent)
                return true
            }
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