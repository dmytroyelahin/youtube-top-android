package com.factory.youtube.network

import com.factory.youtube.model.ChannelsResponse
import com.factory.youtube.model.VideosResponse
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

interface YouTubeApiService {
    @GET("videos")
    suspend fun mostPopular(
        @Query("part") part: String = "snippet,statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "US",
        @Query("maxResults") maxResults: Int = 25,
        @Query("pageToken") pageToken: String? = null,
        @Query("key") key: String
    ): VideosResponse

    @GET("channels")
    suspend fun channelsById(
        @Query("part") part: String = "snippet,statistics",
        @Query("id") idsCsv: String,
        @Query("key") key: String
    ): ChannelsResponse
}

object YouTubeApiFactory {
    private val json = Json { ignoreUnknownKeys = true }

    fun create(): YouTubeApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(YouTubeApiService::class.java)
    }
}
