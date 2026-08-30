package com.example.learningtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningtracker.data.models.Project
import com.example.learningtracker.data.models.Topic
import com.example.learningtracker.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardState(
    val recentTopics: List<Topic> = emptyList(),
    val activeProjects: List<Project> = emptyList(),
    val isLoading: Boolean = true
)

class DashboardViewModel(private val repository: AppRepository) : ViewModel() {

    val uiState: StateFlow<DashboardState> = combine(
        repository.getAllTopics(),
        repository.getAllProjects()
    ) { topics, projects ->
        DashboardState(
            recentTopics = topics.take(5), // Just show 5 recent for dashboard
            activeProjects = projects.filter { it.status != "Completed" }.take(5),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState(isLoading = true)
    )
}
