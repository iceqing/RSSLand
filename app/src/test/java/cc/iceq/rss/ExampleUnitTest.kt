package cc.iceq.rss

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.*

import org.junit.Test

import org.junit.Assert.*
import org.junit.Ignore
import java.net.URL


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Ignore
    @Test
    fun addition_isCorrect1() {
        val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL("https://iceq.cc/atom.xml")))
        println("queryAll = ${feed}")
        val feed2: SyndFeed = SyndFeedInput().build(XmlReader(URL("https://www.ithome.com/rss")))
        println("queryAll = ${feed2}")
    }


    @Ignore
    @Test
    fun addition_isCorrect2() {
        GlobalScope.launch(Dispatchers.IO) {
            val start = System.currentTimeMillis();
            val listDeffer = ArrayList<Deferred<SyndFeed>>()
            val list = ArrayList<SyndFeed>()
            for (i in 1..10) {
                val deffer = async {
                    delay(1000)
                    val feed: SyndFeed =
                        SyndFeedInput().build(XmlReader(URL("https://iceq.cc/atom.xml")))
                    feed
                }
                listDeffer.add(deffer)
            }

            for (deferred in listDeffer) {
                list.add(deferred.await())
            }

            println(System.currentTimeMillis() - start)
            val feed = list.get(0)
            println("queryAll = ${feed.title}")
        }
        println("javaClass = ${javaClass}")
        Thread.sleep(50000)

    }


    @Ignore
    @Test
    fun addition_isCorrect3() {
        GlobalScope.launch(Dispatchers.IO) {
            val start = System.currentTimeMillis();
            val list = ArrayList<SyndFeed>()
            for (i in 1..10) {
                delay(1000)
                val feed: SyndFeed =
                    SyndFeedInput().build(XmlReader(URL("https://iceq.cc/atom.xml")))
                feed
                list.add(feed)
            }

            println(System.currentTimeMillis() - start)
            val feed = list.get(0)
            println("queryAll = ${feed.title}")
        }
        println("javaClass = ${javaClass}")
        Thread.sleep(50000)

    }

}