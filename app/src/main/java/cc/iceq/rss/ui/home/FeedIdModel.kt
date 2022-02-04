package cc.iceq.rss.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedIdModel() : ViewModel() {

    private val _text = MutableLiveData<Long>().apply {
        value = -1
    }
    val text: LiveData<Long> = _text


    fun postId(id: Long) {
        _text.postValue(id)
    }
}