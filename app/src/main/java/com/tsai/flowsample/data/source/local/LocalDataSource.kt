package com.tsai.flowsample.data.source.local

import com.tsai.flowsample.data.Result
import com.tsai.flowsample.data.source.DataSource
import com.tsai.flowsample.ext.toast
import com.tsai.flowsample.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

object LocalDataSource : DataSource {

    override fun getTitle(): Flow<String> = flow {
        Logger.d("getTitle")
        val title = "This title is get from local data source"
        delay(2000)
        emit(title)
    }.flowOn(Dispatchers.IO)

    override fun changeTitleString(): Flow<String> = flow {
        Logger.d("changeTitleString")
        delay(5000)
        val newTitle = "This new title is get from local data source"
        emit(newTitle)
    }.flowOn(Dispatchers.IO)

}