package cc.iceq.rss.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cc.iceq.rss.model.Feed

@Dao
interface FeedDao {

    @Insert
    fun insertFeed(feed:Feed):Long

    @Query("select * from Feed")
    fun findAll():List<Feed>
}