package cc.iceq.rss.util

import android.util.Log
import cc.iceq.rss.model.FeedDetail
import cc.iceq.rss.service.ArticleServiceImpl
import com.rometools.rome.feed.synd.SyndFeed
import kotlinx.coroutines.*
import java.lang.Runnable

object RefreshUtil {

    val articleService = ArticleServiceImpl()

    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("error", "error when request", throwable)
        // 发生异常时的捕获
    }

    fun refresh(id: Long, it: Runnable?) {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                // 执行你的耗时操作代码
                var url = articleService.queryUrlById(id)
                if (url.isNullOrBlank()) {
                    Log.w("WARN", "url不能为空")
                } else {
                    Log.i("INFO", "url is $url")
                    var syncFeed = articleService.findSyncFeedByUrl(url)
                    saveToDb(id, syncFeed, it)
                }

            }
        }
    }

    fun refresh(id: Long) {
        refresh(id, null)
    }

    private suspend fun saveToDb(feedId: Long, syncFeed: SyndFeed?, it: Runnable?) {
        withContext(Dispatchers.Main) {
            syncFeed?.entries?.forEach {
                if (!articleService.containsArticleUrl(it.link)) {
                    var author = it.author
                    if ((author.isNullOrBlank()) && it.authors.size > 0) {
                        author = it.authors[0].name
                    }
                    if (author.isNullOrBlank()) {
                        author = syncFeed.title
                    }
                    val feedDetail =
                        FeedDetail(it.title, it.link, author, feedId, it.publishedDate.time)
                    articleService.insert(feedDetail)
                }
            }
            it?.run()
        }
    }


}