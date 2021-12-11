package com.crownedjester.soft.unsplashimagesearch.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crownedjester.soft.unsplashimagesearch.data.remote.UnsplashApi
import com.crownedjester.soft.unsplashimagesearch.data.remote.dto.PhotoDto
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1
private const val DEFAULT_PAGE_DIFF = 1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, PhotoDto>() {

    override fun getRefreshKey(state: PagingState<Int, PhotoDto>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoDto> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.getPhotosByKeyword(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - DEFAULT_PAGE_DIFF,
                nextKey = if (photos.isEmpty()) null else position + DEFAULT_PAGE_DIFF
            )
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }

}
