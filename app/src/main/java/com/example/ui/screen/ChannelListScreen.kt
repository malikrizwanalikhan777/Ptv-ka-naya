package com.example.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.ChannelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelListScreen(viewModel: ChannelViewModel, onStreamClick: (String) -> Unit) {
    val channels by viewModel.channels.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedChannel by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<com.example.data.model.Channel?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = androidx.compose.ui.graphics.Color(0xFF87CEEB))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "PTV Sports Live TV", style = MaterialTheme.typography.headlineSmall, color = androidx.compose.ui.graphics.Color.White)
            Button(onClick = { viewModel.fetchChannels() }) {
                Text("Refresh")
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(channels) { channel ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(150.dp),
                        onClick = { selectedChannel = channel }
                    ) {
                        Text(text = channel.name, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }

    selectedChannel?.let { channel ->
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { selectedChannel = null }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.8f),
                shape = MaterialTheme.shapes.medium
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(channel.streams) { stream ->
                        Button(
                            onClick = {
                                val url = stream.url
                                selectedChannel = null 
                                onStreamClick(url)
                            },
                            modifier = Modifier.padding(8.dp).fillMaxWidth()
                        ) {
                            Text(text = stream.name)
                        }
                    }
                }
            }
        }
    }
}
