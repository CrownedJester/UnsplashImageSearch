package com.crownedjester.soft.unsplashimagesearch.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoDto(
    val id: String,
    val description: String?,
    val urls: PhotoUrls,
    val user: User
) : Parcelable {

    @Parcelize
    data class PhotoUrls(
        val full: String,
        val raw: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class User(
        val username: String,
        val name: String
    ) : Parcelable {
    }
}