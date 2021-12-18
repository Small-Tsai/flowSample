package com.tsai.flowsample

import com.tsai.flowsample.data.source.DefaultRepo
import com.tsai.flowsample.data.source.local.LocalDataSource
import com.tsai.flowsample.data.source.Repo

object ServiceLocator {

    @Volatile
    private var repo: Repo? = null

    fun provideRepo(): Repo {
        synchronized(this) {
            return repo ?: createRepo()
        }
    }

    private fun createRepo(): Repo {
        return DefaultRepo(LocalDataSource)
    }

}