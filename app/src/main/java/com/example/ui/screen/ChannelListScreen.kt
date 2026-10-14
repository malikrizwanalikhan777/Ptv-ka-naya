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
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import com.example.ui.viewmodel.ChannelViewModel
import com.example.ads.AdsManager
import com.example.ads.UnityBannerAd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelListScreen(viewModel: ChannelViewModel, onStreamClick: (String) -> Unit) {
    val channels by viewModel.channels.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedChannel by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<com.example.data.model.Channel?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val context = LocalContext.current as Activity
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = androidx.compose.ui.graphics.Color(0xFF87CEEB)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "PTV Sports Live TV", style = MaterialTheme.typography.headlineSmall, color = androidx.compose.ui.graphics.Color.White, modifier = Modifier.padding(16.dp))
            Button(onClick = { viewModel.fetchChannels() }, modifier = Modifier.padding(16.dp)) {
                Text("Refresh")
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(channels) { channel ->
                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .height(140.dp),
                            onClick = {
                                if (channel.streams.size == 1) {
                                    viewModel.incrementClickCounter()
                                    val count = viewModel.clickCounter
                                    if (count == 2 || count == 8) AdsManager.showInterstitial(context)
                                    else if (count == 4) AdsManager.showRewarded(context)
                                    onStreamClick(channel.streams[0].url)
                                } else {
                                    selectedChannel = channel
                                }
                            }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(4.dp)) {
                                coil.compose.AsyncImage(
                                    model = channel.image,
                                    contentDescription = channel.name,
                                    modifier = Modifier.weight(1f).fillMaxWidth(),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                                )
                                Text(text = channel.name, modifier = Modifier.padding(4.dp), maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }
        }
        UnityBannerAd(modifier = Modifier.fillMaxWidth().height(50.dp))
    }

    selectedChannel?.let { channel ->
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { selectedChannel = null }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.9f).wrapContentHeight(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Select Stream", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
                    ) {
                        items(channel.streams) { stream ->
                            Button(
                                onClick = {
                                    viewModel.incrementClickCounter()
                                    val count = viewModel.clickCounter
                                    if (count == 2 || count == 8) AdsManager.showInterstitial(context)
                                    else if (count == 4) AdsManager.showRewarded(context)
                                    selectedChannel = null 
                                    onStreamClick(stream.url)
                                },
                                modifier = Modifier.padding(4.dp).fillMaxWidth()
                            ) {
                                Text(text = stream.name)
                            }
                        }
                    }
                }
            }
        }
    }
}
