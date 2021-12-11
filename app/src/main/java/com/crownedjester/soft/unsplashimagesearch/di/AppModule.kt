package com.crownedjester.soft.unsplashimagesearch.di

import com.crownedjester.soft.unsplashimagesearch.common.Constants
import com.crownedjester.soft.unsplashimagesearch.data.remote.UnsplashApi
import com.crownedjester.soft.unsplashimagesearch.domain.repository.UnsplashRepository
import com.crownedjester.soft.unsplashimagesearch.domain.repository.UnsplashRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUnsplashApiService(): UnsplashApi {
        return Retrofit.Builder()
            .baseUrl(Constants.UNSPLASH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: UnsplashApi): UnsplashRepository {
        return UnsplashRepositoryImpl(api)

    }
}