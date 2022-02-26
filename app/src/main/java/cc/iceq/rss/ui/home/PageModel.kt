package cc.iceq.rss.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PageModel() : ViewModel() {

    private val _pageNo = MutableLiveData<Int>().apply {
        value = 1
    }
    val pageNo: LiveData<Int> = _pageNo

    fun postPage(id: Int) {
        _pageNo.postValue(id)
    }
}