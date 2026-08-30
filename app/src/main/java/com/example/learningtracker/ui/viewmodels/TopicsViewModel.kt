package com.example.learningtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningtracker.data.models.Topic
import com.example.learningtracker.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TopicsViewModel(private val repository: AppRepository) : ViewModel() {

    val topics: StateFlow<List<Topic>> = repository.getAllTopics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTopic(name: String, color: String, icon: String) {
        viewModelScope.launch {
            val topic = Topic(
                name = name,
                color = color,
                icon = icon,
                createdAt = System.currentTimeMillis()
            )
            repository.insertTopic(topic)
        }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch {
            repository.deleteTopic(topic)
        }
    }
}
