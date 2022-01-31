package cc.iceq.rss.service

import cc.iceq.rss.RssApplication
import cc.iceq.rss.dao.AppDatabase
import cc.iceq.rss.model.ArticleInfo
import cc.iceq.rss.model.Feed
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL

class ArticleServiceImpl : ArticleService {

    val feedDao = AppDatabase.getDatabase(RssApplication.content).feedDao();

    override fun queryAll(url: String): SyndFeed {
//        Log.i("INFO", "url is $url")
        val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
//        Log.i("INFO", "list is $feed")
        return feed
    }


    override fun insert(feed: Feed): Long {
        return feedDao.insertFeed(feed)
    }

    override fun queryUrlById(itemId: Int): String {
        return feedDao.findByItemId(itemId.toLong()).get(0).url
    }

    override fun queryAll(): ArrayList<ArticleInfo> {
        val list= ArrayList<ArticleInfo>();
        val findAll = feedDao.findAll()

        findAll.forEach {item ->
                val name = item.name
                val url =item.url
                val author = item.author

                val articleInfo = ArticleInfo(item.id);
                articleInfo.title = name;
                articleInfo.url = url
                articleInfo.author = author
                articleInfo.desc = name
                list.add(articleInfo)
        }

        return list;
    }
}




