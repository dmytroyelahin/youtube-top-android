package com.factory.youtube.core

data class Video(val views: Long, val channel: Channel)
data class Channel(val subscribers: Long, val createdAtIso: String)

fun filterVideos(
    data: List<Video>,
    minViews: Long?,
    minSubscribers: Long?,
    createdAfterIsoDate: String?
): List<Video> {
    return data.filter { v ->
        val viewsOk = minViews?.let { v.views >= it } ?: true
        val subsOk = minSubscribers?.let { v.channel.subscribers >= it } ?: true
        val dateOk = createdAfterIsoDate?.let { cutoff -> v.channel.createdAtIso >= cutoff } ?: true
        viewsOk && subsOk && dateOk
    }
}
