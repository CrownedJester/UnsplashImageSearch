package com.crownedjester.soft.unsplashimagesearch.data.remote

import com.crownedjester.soft.unsplashimagesearch.data.remote.dto.PhotoDto

data class Response(
    val results: List<PhotoDto>
)
