package com.tsai.flowsample.data.source

import com.tsai.flowsample.data.Result
import kotlinx.coroutines.flow.Flow

class DefaultRepo(
    private val dataSource: DataSource,
) : Repo {

    override suspend fun getTitle(): Flow<String> {
        return dataSource.getTitle()
    }

    override suspend fun changeTitleString(): Flow<String> {
        return dataSource.changeTitleString()
    }

}