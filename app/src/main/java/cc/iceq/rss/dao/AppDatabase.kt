package cc.iceq.rss.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cc.iceq.rss.model.Feed
import cc.iceq.rss.model.FeedDetail

@Database(version = 4, entities = [Feed::class, FeedDetail::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao

    abstract fun feedDetailDao(): FeedDetailDao

    companion object {
        private var instance: AppDatabase? = null

//        val MIGRATION_2_3 = object : Migration(2,3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("create table feed_detail02(id integer primary key autoincrement not null, title text not null, url text not null, author text not null, feed_id bigint default 0 not null, pubDate text not null)")
//            }
//        }

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "RssLand06.db"
            ).allowMainThreadQueries()
//                .addMigrations(MIGRATION_2_3)
                .build().apply {
                instance = this
            }
        }
    }
}