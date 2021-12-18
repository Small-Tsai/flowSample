package com.tsai.flowsample.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tsai.flowsample.data.source.Repo
import com.tsai.flowsample.ui.main.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(
    val repo: Repo,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repo)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}