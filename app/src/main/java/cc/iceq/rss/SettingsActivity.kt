package cc.iceq.rss

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import cc.iceq.rss.util.ThemeUtil
import cc.iceq.rss.util.ToastUtil
import kotlinx.android.synthetic.main.rss_list_activity.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val themePreference = findPreference<ListPreference>("theme_preference")
            if (themePreference != null) {
                themePreference.setOnPreferenceChangeListener { preference, newValue ->
                    ThemeUtil.refreshTheme(newValue)
                    ToastUtil.showShortText("主题已生效")
                    true
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //监听左上角的返回箭头
        val home = android.R.id.home
        val itemId = item.itemId
        Log.i("[INFO] ", "home: $home， itemId: $itemId")

        if (itemId == home) {
            Log.i("[INFO] ", "item eq home id")
            this.finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}