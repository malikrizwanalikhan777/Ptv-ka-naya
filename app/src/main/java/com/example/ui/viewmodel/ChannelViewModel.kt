package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Channel
import com.example.data.remote.RetrofitClient
import com.example.data.repository.ChannelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChannelViewModel : ViewModel() {
    private val repository = ChannelRepository(RetrofitClient.apiService)

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> = _channels

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _clickCounter = 0
    val clickCounter: Int get() = _clickCounter

    fun incrementClickCounter() {
        _clickCounter++
    }

    init {
        fetchChannels()
    }

    fun getChannelById(id: String): Channel? {
        return _channels.value.find { it.id == id }
    }

    fun fetchChannels() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _channels.value = repository.getChannels().channels
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
