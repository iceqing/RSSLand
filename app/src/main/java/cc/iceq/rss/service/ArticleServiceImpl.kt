package cc.iceq.rss.service

import android.content.Context
import cc.iceq.rss.RssApplication
import cc.iceq.rss.dao.MyDatabaseHelper
import cc.iceq.rss.model.ArticleInfo
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL

class ArticleServiceImpl : ArticleService {
    override fun queryAll(url: String): SyndFeed {
//        Log.i("INFO", "url is $url")
        val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
//        Log.i("INFO", "list is $feed")
        return feed
    }


    override fun queryMock(): ArrayList<ArticleInfo> {

        val dbHelper = MyDatabaseHelper(RssApplication.content, "rss02.db", 1)
        val db = dbHelper.writableDatabase

        val cursor = db.query("rss01", null, null, null, null, null, null)

        val list= ArrayList<ArticleInfo>();

        if (cursor.moveToFirst()) {
            do {
                val nameIndex = cursor.getColumnIndex("name")
                val name = cursor.getString(nameIndex)

                val urlIndex = cursor.getColumnIndex("url")
                val url = cursor.getString(urlIndex)

                val authorIndex = cursor.getColumnIndex("author")
                val author = cursor.getString(authorIndex)

                val arrticle1 = ArticleInfo();
                arrticle1.title = name;
                arrticle1.url = url
                arrticle1.author = author
                arrticle1.desc = name
                list.add(arrticle1)
            }while (cursor.moveToNext())
        }

        return list;
    }
}




