package cc.iceq.rss.util

import android.widget.Toast
import cc.iceq.rss.RssApplication

object ToastUtil {

    fun makeShortText(msg:String): Toast {
        return Toast.makeText(RssApplication.context, msg, Toast.LENGTH_SHORT)
    }
}