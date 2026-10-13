package com.example.data.repository

import com.example.data.model.ChannelResponse
import com.example.data.remote.ChannelApiService

class ChannelRepository(private val apiService: ChannelApiService) {
    suspend fun getChannels(): ChannelResponse = apiService.getChannels()
}
