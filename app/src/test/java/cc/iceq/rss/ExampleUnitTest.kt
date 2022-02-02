package cc.iceq.rss

import cc.iceq.rss.service.ArticleServiceImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader

import org.junit.Test

import org.junit.Assert.*
import java.net.URL


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_isCorrect1() {
        val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL("https://iceq.cc/atom.xml")))
        println("queryAll = ${feed}")
        val feed2: SyndFeed = SyndFeedInput().build(XmlReader(URL("https://www.ithome.com/rss")))
        println("queryAll = ${feed2}")
    }
}