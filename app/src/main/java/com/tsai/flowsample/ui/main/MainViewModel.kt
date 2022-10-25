package com.tsai.flowsample.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsai.flowsample.data.source.Repo
import com.tsai.flowsample.ext.toast
import com.tsai.flowsample.util.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class MainViewModel(private val repo: Repo) : ViewModel() {

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Logger.e("$throwable")
        }
    val coroutineScope =
        CoroutineScope(SupervisorJob() + coroutineExceptionHandler + Dispatchers.Main)

    private val _status = MutableStateFlow<LoadApiStatus?>(null)
    val status: StateFlow<LoadApiStatus?> = _status

    private val _titleString = MutableStateFlow("Default")
    var titleString: StateFlow<String> = _titleString

    private val _navToBlankFrag = MutableSharedFlow<Boolean>()
    val navToBlankFrag: SharedFlow<Boolean> = _navToBlankFrag

    private val _isButtonLoading = MutableStateFlow(false)
    val isButtonLoading = _isButtonLoading.asStateFlow()

    private val _setTextEvent = Channel<Unit>(Channel.CONFLATED)
    val setTextEvent = _setTextEvent.receiveAsFlow()

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

    @Volatile
    var i = 0

    init {
        viewModelScope.launch {
            repeat(10000) {
                launch(Dispatchers.IO) {
                    plus()
                }
            }
            Log.i("small tsai", "$i ")
        }
    }

    @Synchronized
    fun plus() {
        i++
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

    fun startDemoProgressButton() {
        viewModelScope.launch {
            _isButtonLoading.update { true }
            delay(4000)
            _setTextEvent.send(Unit)
            Log.d("small tsai", "startDemoProgressButton: sended")
            delay(4000)
            _isButtonLoading.update { false }
        }
    }
}
