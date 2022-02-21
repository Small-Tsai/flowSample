package com.tsai.flowsample.data.source

import com.tsai.flowsample.data.Result
import com.tsai.flowsample.ui.main.LoadApiStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


interface Repo {

    suspend fun getTitle(): Flow<String>

    suspend fun changeTitleString(): Flow<String>

    suspend fun getLargeList(): Flow<List<String>>

}
