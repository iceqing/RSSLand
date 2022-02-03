package cc.iceq.rss.util

import androidx.appcompat.app.AppCompatDelegate

object ThemeUtil {

    fun refreshTheme(newValue: Any?) {
        when (newValue) {
            "day" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            "night" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            "auto" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        }
    }
}