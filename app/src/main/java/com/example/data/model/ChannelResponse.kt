package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChannelResponse(
    val version: Int,
    val channels: List<Channel>
)
