package cc.iceq.rss.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cc.iceq.rss.model.Feed
import cc.iceq.rss.model.FeedDetail

@Dao
interface FeedDetailDao {

    @Insert
    fun insert(feedDetail: FeedDetail): Long

    @Query("select * from feed_detail where feedId=:feedId order by publishTime desc")
    fun findByFeedId(feedId: Long): List<FeedDetail>

    @Query("select * from feed_detail where url=:url")
    fun findByUrl(url:String):List<FeedDetail>
}