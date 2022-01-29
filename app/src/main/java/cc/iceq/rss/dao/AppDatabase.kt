package cc.iceq.rss.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cc.iceq.rss.model.Feed

@Database(version=2, entities = [Feed::class])
abstract class AppDatabase :RoomDatabase() {
    abstract fun feedDao():FeedDao

    companion object {
        private var instance:AppDatabase?=null

        @Synchronized
        fun getDatabase(context: Context):AppDatabase {
            instance?.let {
                return it
            }

            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,
                "RssLand.db").
            allowMainThreadQueries().build().apply {
                    instance=this
            }
        }
    }
}