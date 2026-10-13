package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Channel(
    val id: String,
    val name: String,
    val image: String,
    val streams: List<Stream>
)
