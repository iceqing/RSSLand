package cc.iceq.rss.service

import com.rometools.rome.feed.synd.SyndFeed

interface ArticleService {
    fun queryAll(url:String): SyndFeed
}