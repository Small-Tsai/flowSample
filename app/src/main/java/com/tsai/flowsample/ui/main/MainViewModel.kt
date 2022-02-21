package com.tsai.flowsample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsai.flowsample.data.source.Repo
import com.tsai.flowsample.ext.toast
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repo: Repo) : ViewModel() {

    private val _status = MutableStateFlow<LoadApiStatus?>(null)
    val status: StateFlow<LoadApiStatus?> = _status

    private val _titleString = MutableStateFlow("Default")
    var titleString: StateFlow<String> = _titleString

    private val _navToBlankFrag = MutableSharedFlow<Boolean>()
    val navToBlankFrag: SharedFlow<Boolean> = _navToBlankFrag

    private fun loading() {
        _status.value = LoadApiStatus.LOADING
    }

    private fun complete() {
        _status.value = LoadApiStatus.DONE
    }

    private fun setTitle(title: String) {
        _titleString.value = title
    }

    fun navToBlack() {
        viewModelScope.launch {
            _navToBlankFrag.emit(true)
        }
    }


    @FlowPreview
    fun getTilteThenChangeTitle() {
        viewModelScope.launch {
            repo.getTitle()
                .onStart { loading() }
                .onCompletion { complete() }
                .flatMapConcat { string ->
                    repo.changeTitleString()
                        .map { newStr ->
                            string + newStr
                        }
                }.collectLatest {
                    setTitle(it)
                }
        }
    }

    @FlowPreview
    fun getBothAtSameTime() {
        viewModelScope.launch {
            repo.getTitle()
                .onStart { loading() }
                .zip(repo.changeTitleString()) { str, newStr ->
                    str + newStr
                }
                .onCompletion { complete() }
                .collectLatest {
                    setTitle(it)
                }
        }
    }

    fun getTitleStr() {
        viewModelScope.launch {
            repo.getTitle()
                .map { "$it + using flow" }
                .onStart { loading() }
                .onCompletion { complete() }
                .catch { "$it".toast() }
                .collectLatest {
                    setTitle(it)
                }
        }
    }

    fun changeTitleString() {
        viewModelScope.launch {
            repo.changeTitleString()
                .onStart { loading() }
                .onCompletion { complete() }
                .catch { "$it".toast() }
                .collectLatest {
                    setTitle(it)
                }
        }
    }
}
