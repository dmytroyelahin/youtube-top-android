package com.factory.youtube.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.factory.youtube.model.VideoWithChannel
import com.factory.youtube.repo.YoutubeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val videos: List<VideoWithChannel> = emptyList(),
    val minViews: Long? = null,
    val minSubscribers: Long? = null,
    val createdAfter: String? = null
)

@HiltViewModel
class TopVideosViewModel @Inject constructor(
    private val repo: YoutubeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private var raw: List<VideoWithChannel> = emptyList()

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        try {
            val (videos, _) = repo.fetchTopVideos()
            raw = repo.enrichWithChannels(videos)
            _uiState.value = _uiState.value.copy(isLoading = false, videos = applyCurrentFilters())
        } catch (t: Throwable) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = t.message)
        }
    }

    fun applyFilters(minViewsTxt: String, minSubsTxt: String, dateIso: String) {
        val mv = minViewsTxt.toLongOrNull()
        val ms = minSubsTxt.toLongOrNull()
        val d = dateIso.ifBlank { null }
        _uiState.value = _uiState.value.copy(minViews = mv, minSubscribers = ms, createdAfter = d, videos = applyCurrentFilters())
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(minViews = null, minSubscribers = null, createdAfter = null, videos = raw)
    }

    private fun applyCurrentFilters(): List<VideoWithChannel> {
        val s = _uiState.value
        return repo.filter(raw, s.minViews, s.minSubscribers, s.createdAfter)
    }
}
