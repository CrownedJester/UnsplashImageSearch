package com.crownedjester.soft.unsplashimagesearch.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.crownedjester.soft.unsplashimagesearch.data.remote.dto.PhotoDto

interface UnsplashRepository {

    fun getPhotosByKeyword(query: String): LiveData<PagingData<PhotoDto>>
}