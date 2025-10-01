package com.factory.youtube.core

import org.junit.Assert.assertEquals
import org.junit.Test

class FiltersTest {
    @Test
    fun testFilters() {
        val c1 = Channel(subscribers = 1000, createdAtIso = "2020-01-01")
        val c2 = Channel(subscribers = 10, createdAtIso = "2009-01-01")
        val v1 = Video(views = 100, channel = c1)
        val v2 = Video(views = 1_000_000, channel = c2)
        val list = listOf(v1, v2)

        assertEquals(listOf(v1), filterVideos(list, minViews = 50, minSubscribers = 500, createdAfterIsoDate = "2015-01-01"))
        assertEquals(emptyList<Video>(), filterVideos(list, minViews = 1000, minSubscribers = 100, createdAfterIsoDate = "2010-01-01"))
        assertEquals(list, filterVideos(list, null, null, null))
    }
}
