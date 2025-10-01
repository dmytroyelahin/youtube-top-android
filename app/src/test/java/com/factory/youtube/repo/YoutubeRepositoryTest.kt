package com.factory.youtube.repo

import com.factory.youtube.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class YoutubeRepositoryTest {
    private val dummyRepo = YoutubeRepository(api = object : com.factory.youtube.network.YouTubeApiService {
        override suspend fun mostPopular(part: String, chart: String, regionCode: String, maxResults: Int, pageToken: String?, key: String): VideosResponse =
            VideosResponse(emptyList())
        override suspend fun channelsById(part: String, idsCsv: String, key: String): ChannelsResponse =
            ChannelsResponse(emptyList())
    })

    @Test
    fun testFilterViewsSubsDate() {
        val v1 = VideoItem("v1", VideoSnippet(title = "A", channelId = "c1", publishedAt = "2020-01-01T00:00:00Z"), VideoStatistics("100"))
        val v2 = VideoItem("v2", VideoSnippet(title = "B", channelId = "c2", publishedAt = "2010-01-01T00:00:00Z"), VideoStatistics("1000000"))
        val c1 = ChannelItem("c1", ChannelSnippet(title = "C1", publishedAt = "2019-01-01T00:00:00Z"), ChannelStatistics("1000"))
        val c2 = ChannelItem("c2", ChannelSnippet(title = "C2", publishedAt = "2009-01-01T00:00:00Z"), ChannelStatistics("10"))
        val list = listOf(
            VideoWithChannel(v1, c1),
            VideoWithChannel(v2, c2)
        )
        val filtered = dummyRepo.filter(list, minViews = 1000, minSubscribers = 100, createdAfterIsoDate = "2010-01-01")
        // Only v2 has views >= 1000, but channel c2 has subs 10 (<100) and created 2009 (< 2010) => none
        assertEquals(0, filtered.size)

        val filtered2 = dummyRepo.filter(list, minViews = 50, minSubscribers = 500, createdAfterIsoDate = "2015-01-01")
        // v1 passes: views 100, subs 1000, channel created 2019 (>2015)
        assertEquals(listOf(v1.id), filtered2.map { it.video.id })
    }
}
