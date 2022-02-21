package com.tsai.flowsample.blank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsai.flowsample.data.source.Repo
import com.tsai.flowsample.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlankFragmentViewModel(private val repo: Repo) : ViewModel() {

    private val _largeList = MutableStateFlow<List<String>>(emptyList())
    val largeList: StateFlow<List<String>> get() = _largeList

    var storedList = emptyList<String>()


    init {
        viewModelScope.launch {
            repo.getLargeList()
                .collectLatest {
                    _largeList.emit(it)
                    storedList = it
                }
        }
    }

    var times = 1

    fun updateList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                Logger.d("time = $times")
                var new = listOf<String>()
                when (times) {
                    1 -> {
                        new = storedList.filter {
                            it.toInt() > 20
                        }
                    }
                    2 -> {
                        new = storedList.filter {
                            it.toInt() % 2 == 0
                        }
//                        Logger.d("$new")
                    }
                    3 -> {
                        new = storedList
                    }
                }
                _largeList.emit(new)

                times++

                if (times == 4) {
                    times = 1
                }
            }
        }
    }

}