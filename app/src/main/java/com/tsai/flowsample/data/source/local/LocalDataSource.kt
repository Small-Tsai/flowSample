package com.tsai.flowsample.data.source.local

import com.tsai.flowsample.data.source.DataSource
import com.tsai.flowsample.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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

    override fun getLargeList(): Flow<List<String>> = flow {
        val list = mutableListOf<String>()
        for (i in 0..300000) {
            list.add(i.toString())
        }
        emit(list)
        Logger.d("$list")
    }.flowOn(Dispatchers.IO)

}