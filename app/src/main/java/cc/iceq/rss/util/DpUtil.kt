package cc.iceq.rss.util

import android.content.res.Resources
import android.util.TypedValue

object DpUtil {

    fun dpToPixel(dp: Float, resources: Resources) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

}