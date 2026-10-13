package com.example.data.remote

import com.example.data.model.ChannelResponse
import retrofit2.http.GET

interface ChannelApiService {
    @GET("channels.json")
    suspend fun getChannels(): ChannelResponse
}
