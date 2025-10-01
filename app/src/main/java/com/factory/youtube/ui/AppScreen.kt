package com.factory.youtube.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.factory.youtube.viewmodel.TopVideosViewModel

@Composable
fun AppScreen(vm: TopVideosViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text(text = "Filters", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        FilterRow(
            minViews = state.minViews?.toString().orEmpty(),
            minSubscribers = state.minSubscribers?.toString().orEmpty(),
            createdAfter = state.createdAfter.orEmpty(),
            onApply = { v, s, d -> vm.applyFilters(v, s, d) },
            onClear = { vm.clearFilters() }
        )
        Spacer(Modifier.height(12.dp))
        when {
            state.isLoading -> CircularProgressIndicator()
            state.error != null -> Text("Error: ${state.error}")
            else -> LazyColumn(Modifier.fillMaxSize()) {
                items(state.videos) { item ->
                    VideoRow(item)
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    minViews: String,
    minSubscribers: String,
    createdAfter: String,
    onApply: (String, String, String) -> Unit,
    onClear: () -> Unit
) {
    var views by remember { mutableStateOf(TextFieldValue(minViews)) }
    var subs by remember { mutableStateOf(TextFieldValue(minSubscribers)) }
    var date by remember { mutableStateOf(TextFieldValue(createdAfter)) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(views, { views = it }, label = { Text("Min views") })
        OutlinedTextField(subs, { subs = it }, label = { Text("Min subscribers") })
        OutlinedTextField(date, { date = it }, label = { Text("YYYY-MM-DD") })
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onApply(views.text, subs.text, date.text) }) { Text("Apply") }
            OutlinedButton(onClick = onClear) { Text("Clear") }
        }
    }
}

@Composable
private fun VideoRow(item: com.factory.youtube.model.VideoWithChannel) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(item.video.snippet.title, style = MaterialTheme.typography.titleMedium)
        Text("Views: ${item.video.statistics.viewCount} • Channel: ${item.channel?.snippet?.title ?: "?"}")
        if (item.channel != null) {
            Text("Subs: ${item.channel.statistics.subscriberCount} • Created: ${item.channel.snippet.publishedAt.substring(0, 10)}")
        }
    }
}
