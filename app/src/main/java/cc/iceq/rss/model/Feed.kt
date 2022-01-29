package cc.iceq.rss.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed")
data class Feed(var name:String, var url:String, var author:String) {
    @PrimaryKey(autoGenerate = true)
    var id:Long=0

}