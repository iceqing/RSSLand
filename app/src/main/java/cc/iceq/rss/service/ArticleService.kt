package cc.iceq.rss.service

import cc.iceq.rss.model.ArticleInfo
import cc.iceq.rss.model.Feed
import com.rometools.rome.feed.synd.SyndFeed

interface ArticleService {
    fun findSyncFeedByUrl(url:String): SyndFeed?
    fun queryAll(): ArrayList<ArticleInfo>
    fun insert(feed: Feed): Long
    fun queryUrlById(itemId: Int): String
}