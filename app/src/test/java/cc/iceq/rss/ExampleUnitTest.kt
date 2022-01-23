package cc.iceq.rss

import cc.iceq.rss.service.ArticleServiceImpl

import org.junit.Test

import org.junit.Assert.*




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
        var a = ArticleServiceImpl();
        val queryAll = a.queryAll("https://iceq.cc/atom.xml")
        println("queryAll = ${queryAll}")
    }
}