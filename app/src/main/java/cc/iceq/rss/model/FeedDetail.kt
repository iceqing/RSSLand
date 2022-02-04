package cc.iceq.rss.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_detail")
data class FeedDetail(var title: String,
                      var url: String,
                      var author: String,
                      var feedId: Long,
                      var pubDate: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}