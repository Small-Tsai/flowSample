package com.tsai.flowsample.ext

import android.app.Application
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tsai.flowsample.MyApplication
import com.tsai.flowsample.NavigationDirections
import com.tsai.flowsample.factory.ViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Fragment.getVmFactory(): ViewModelFactory {
    val repo = (requireContext().applicationContext as MyApplication).repo
    return ViewModelFactory(repo)
}

fun <T> Fragment.collectLifeCycleFlowStarted(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun <T> Fragment.collectLatestLifeCycleFlowStarted(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}