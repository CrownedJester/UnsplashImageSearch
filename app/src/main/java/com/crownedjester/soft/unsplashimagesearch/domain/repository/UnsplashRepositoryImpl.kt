package com.crownedjester.soft.unsplashimagesearch.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.crownedjester.soft.unsplashimagesearch.data.remote.UnsplashApi
import com.crownedjester.soft.unsplashimagesearch.domain.UnsplashPagingSource
import javax.inject.Inject
import javax.inject.Singleton

class UnsplashRepositoryImpl @Inject constructor(
    private val unsplashApi: UnsplashApi
) : UnsplashRepository {
    override fun getPhotosByKeyword(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData


}