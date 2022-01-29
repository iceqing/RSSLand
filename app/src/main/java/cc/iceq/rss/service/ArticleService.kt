package cc.iceq.rss.service

import cc.iceq.rss.model.ArticleInfo
import com.rometools.rome.feed.synd.SyndFeed

interface ArticleService {
    fun queryAll(url:String): SyndFeed
    fun queryMock(): ArrayList<ArticleInfo>
}