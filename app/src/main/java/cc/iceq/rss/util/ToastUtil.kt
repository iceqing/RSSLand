package cc.iceq.rss.util

import android.widget.Toast
import cc.iceq.rss.RssApplication

object ToastUtil {

    fun showShortText(msg:String) {
        return Toast.makeText(RssApplication.context, msg, Toast.LENGTH_SHORT).show()
    }
}