package com.factory.youtube.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(val totalResults: Int = 0, val resultsPerPage: Int = 0)

@Serializable
data class VideosResponse(
    val items: List<VideoItem> = emptyList(),
    val nextPageToken: String? = null,
    val pageInfo: PageInfo = PageInfo()
)

@Serializable
data class ChannelsResponse(
    val items: List<ChannelItem> = emptyList()
)

@Serializable
data class VideoItem(
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics
)

@Serializable
data class VideoSnippet(
    val title: String = "",
    val description: String = "",
    val channelId: String = "",
    val channelTitle: String = "",
    val publishedAt: String = ""
)

@Serializable
data class VideoStatistics(
    @SerialName("viewCount") val viewCount: String = "0"
)

@Serializable
data class ChannelItem(
    val id: String,
    val snippet: ChannelSnippet,
    val statistics: ChannelStatistics
)

@Serializable
data class ChannelSnippet(
    val title: String = "",
    val publishedAt: String = ""
)

@Serializable
data class ChannelStatistics(
    @SerialName("subscriberCount") val subscriberCount: String = "0"
)

data class VideoWithChannel(
    val video: VideoItem,
    val channel: ChannelItem?
)
