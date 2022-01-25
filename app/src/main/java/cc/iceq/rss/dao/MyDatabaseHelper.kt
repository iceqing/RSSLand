package cc.iceq.rss.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name:String, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    private val createRss1 = "create table rss01(id integer primary key autoincrement, author text, name text, url text)"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createRss1)
        val toast = Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}