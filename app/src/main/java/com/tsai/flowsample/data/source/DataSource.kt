package com.tsai.flowsample.data.source

import com.tsai.flowsample.data.Result
import kotlinx.coroutines.flow.Flow

interface DataSource {

    fun getTitle(): Flow<String>

    fun changeTitleString(): Flow<String>

}