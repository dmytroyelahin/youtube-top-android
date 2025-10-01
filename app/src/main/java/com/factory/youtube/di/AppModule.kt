package com.factory.youtube.di

import com.factory.youtube.network.YouTubeApiFactory
import com.factory.youtube.network.YouTubeApiService
import com.factory.youtube.repo.YoutubeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApi(): YouTubeApiService = YouTubeApiFactory.create()

    @Provides
    @Singleton
    fun provideRepo(api: YouTubeApiService): YoutubeRepository = YoutubeRepository(api)
}
