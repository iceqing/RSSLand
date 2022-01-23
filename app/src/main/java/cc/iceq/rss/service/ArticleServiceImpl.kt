package cc.iceq.rss.service

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.net.URL

class ArticleServiceImpl : ArticleService {
    override fun queryAll(url:String): SyndFeed {
//        Log.i("INFO", "url is $url")
        val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
//        Log.i("INFO", "list is $feed")
        return feed
    }
}




