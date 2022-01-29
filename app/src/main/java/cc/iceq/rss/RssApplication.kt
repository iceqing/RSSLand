package cc.iceq.rss

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class RssApplication : Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var content:Context;
    }

    override fun onCreate() {
        super.onCreate()
        content = applicationContext;
    }
}