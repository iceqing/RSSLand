package cc.iceq.rss

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cc.iceq.rss.dao.MyDatabaseHelper
import cc.iceq.rss.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding

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

        val btn: Button = findViewById(R.id.note_save_btn)
        btn.setOnClickListener {
            val dbHelper = MyDatabaseHelper(this, "rss02.db", 1)
            val db = dbHelper.writableDatabase

            var name: String = findViewById<EditText>(R.id.rss_name_text).text.toString()
            if (null == name) {
                name = ""
            }

            val values1 = ContentValues().apply {
                put("name", name)
                put("author", "author1")
                put("url", findViewById<EditText>(R.id.rss_url_text).text.toString())
            }
            db.insert("rss01", null, values1)
            val toast = Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT)
            toast.show();
            this.finish()
        }
    }

    private var isOpenEye = false

    override fun onResume() {
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
