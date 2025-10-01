package com.factory.youtube.repo

import com.factory.youtube.BuildConfig
import com.factory.youtube.model.ChannelItem
import com.factory.youtube.model.VideoItem
import com.factory.youtube.model.VideoWithChannel
import com.factory.youtube.network.YouTubeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YoutubeRepository(private val api: YouTubeApiService) {
    private val apiKey: String get() = BuildConfig.YOUTUBE_API_KEY

    suspend fun fetchTopVideos(
        pageToken: String? = null
    ): Pair<List<VideoItem>, String?> = withContext(Dispatchers.IO) {
        val resp = api.mostPopular(key = apiKey, pageToken = pageToken)
        resp.items to resp.nextPageToken
    }

    suspend fun enrichWithChannels(videos: List<VideoItem>): List<VideoWithChannel> = withContext(Dispatchers.IO) {
        val channelIds = videos.map { it.snippet.channelId }.distinct().takeIf { it.isNotEmpty() } ?: return@withContext videos.map { VideoWithChannel(it, null) }
        val channelsResp = api.channelsById(idsCsv = channelIds.joinToString(","), key = apiKey)
        val channelsById = channelsResp.items.associateBy { it.id }
        videos.map { v -> VideoWithChannel(v, channelsById[v.snippet.channelId]) }
    }

    fun filter(
        data: List<VideoWithChannel>,
        minViews: Long?,
        minSubscribers: Long?,
        createdAfterIsoDate: String?
    ): List<VideoWithChannel> {
        return data.filter { item ->
            val viewsOk = minViews?.let { (item.video.statistics.viewCount.toLongOrNull() ?: 0L) >= it } ?: true
            val subsOk = minSubscribers?.let { (item.channel?.statistics?.subscriberCount?.toLongOrNull() ?: 0L) >= it } ?: true
            val dateOk = createdAfterIsoDate?.let { cutoff ->
                val chDate = item.channel?.snippet?.publishedAt
                chDate != null && chDate >= cutoff
            } ?: true
            viewsOk && subsOk && dateOk
        }
    }
}
