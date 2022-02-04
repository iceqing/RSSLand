package cc.iceq.rss.service

import cc.iceq.rss.model.ArticleInfo
import cc.iceq.rss.model.Feed
import cc.iceq.rss.model.FeedDetail
import com.rometools.rome.feed.synd.SyndFeed

interface ArticleService {
    fun findSyncFeedByUrl(url:String): SyndFeed?
    fun queryAll(): ArrayList<ArticleInfo>
    fun insert(feed: Feed): Long
    fun queryUrlById(feedId: Long): String
    fun findFeedDetailById(feedId: Long): List<FeedDetail>
    fun insert(feedDetail: FeedDetail): Long
    fun containsUrl(link: String?): Boolean
}